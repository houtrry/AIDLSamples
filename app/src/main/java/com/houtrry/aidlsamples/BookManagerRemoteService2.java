package com.houtrry.aidlsamples;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.houtrry.aidlsamples.impl.Book;
import com.houtrry.aidlsamples.impl.BookManagerImpl;
import com.houtrry.aidlsamples.impl.INewBookArrivedListener;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: AIDL的服务端程序
 */

public class BookManagerRemoteService2 extends Service {

    private static final String TAG = BookManagerRemoteService2.class.getSimpleName();

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
//    private CopyOnWriteArrayList<INewBookArrivedListener> mINewBookArrivedListeners = new CopyOnWriteArrayList<>();

    /**
     * !!!重要
     * 这里为什么要使用RemoteCallbackList!!!
     * 因为客户端调用服务端的方法的时候, 比如说removeNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener), 传的参数newBookArrivedListener
     * 与我们在服务端的removeNewBookArrivedListener方法里接收到的参数newBookArrivedListener并不是同一个
     * 可以参考AIDLSamples\app\build\generated\source\aidl\debug\com\houtrry\aidlsamples\aidl\IBookManager.java
     * 服务端接收到的是一个新的INewBookArrivedListener对象.
     * 这就导致了一个问题, 如果用CopyOnWriteArrayList, mINewBookArrivedListeners.contains(newBookArrivedListener)会返回FALSE
     */
    private RemoteCallbackList<INewBookArrivedListener> mINewBookArrivedListeners = new RemoteCallbackList<>();

    private AtomicBoolean isDestroy = new AtomicBoolean();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
        isDestroy.set(false);
        mBookList.add(new Book(0x0061, "唐吉坷德"));
        mBookList.add(new Book(0x0062, "堕落"));
        mBookList.add(new Book(0x0063, "尤利西斯"));

        new Thread(new ServiceWorkRunner()).start();
    }

    @Override
    public void onDestroy() {
        isDestroy.set(true);
        super.onDestroy();
    }

    private BookManagerImpl mIBookManager = new BookManagerImpl() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.d(TAG, "getBookList: ThreadName: " + Thread.currentThread().getName());
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.d(TAG, "addBook: ThreadName: " + Thread.currentThread().getName());
            if (mBookList != null) {
                mBookList.add(book);
            }
        }

        @Override
        public boolean addNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
//            if (mINewBookArrivedListeners.contains(newBookArrivedListener)) {
//                Log.d(TAG, "addNewBookArrivedListener: has add this listener");
//                return false;
//            }
            mINewBookArrivedListeners.register(newBookArrivedListener);
            return true;
        }

        @Override
        public boolean removeNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
//            if (!mINewBookArrivedListeners.contains(newBookArrivedListener)) {
//                Log.e(TAG, "addNewBookArrivedListener: don't add this listener");
//                return false;
//            }

            /**
             * mINewBookArrivedListeners.beginBroadcast()和mINewBookArrivedListeners.finishBroadcast();必须一起使用
             */

            Log.d(TAG, "removeNewBookArrivedListener: " + mINewBookArrivedListeners.beginBroadcast());
            mINewBookArrivedListeners.finishBroadcast();
            mINewBookArrivedListeners.unregister(newBookArrivedListener);
            Log.d(TAG, "removeNewBookArrivedListener: " + mINewBookArrivedListeners.beginBroadcast());
            mINewBookArrivedListeners.finishBroadcast();
            return true;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mIBookManager != null) {
                mIBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mIBookManager = null;
                // TODO: 2017/10/29 在这里重新绑定
            }
        }
    };


    private void arrivedNewBook(Book book) throws RemoteException {
        mBookList.add(book);
//        for (INewBookArrivedListener listener : mINewBookArrivedListeners) {
//            listener.onNewBookArrived(book);
//        }

        final int length = mINewBookArrivedListeners.beginBroadcast();
        for (int i = 0; i < length; i++) {
            INewBookArrivedListener listener = mINewBookArrivedListeners.getBroadcastItem(i);
            if (listener != null) {
                //针对每一个try-catch的好处是, 一个失败了, 不会影响循环的继续执行
                try {
                    listener.onNewBookArrived(book);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "arrivedNewBook: " + e);
                }
            }
        }
        mINewBookArrivedListeners.finishBroadcast();
    }

    private class ServiceWorkRunner implements Runnable {

        @Override
        public void run() {
            while (!isDestroy.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    arrivedNewBook(new Book(new Random().nextInt(10000), "## book name " + System.currentTimeMillis()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
