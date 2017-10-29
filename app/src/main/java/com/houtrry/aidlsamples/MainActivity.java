package com.houtrry.aidlsamples;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.houtrry.aidlsamples.aidl.Book;
import com.houtrry.aidlsamples.aidl.IBookManager;
import com.houtrry.aidlsamples.aidl.INewBookArrivedListener;

import java.util.List;

/**
 * @author: houtrry
 * @time: 2017/10/29
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private IBookManager mBookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookManagerRemoteService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ThreadName: "+Thread.currentThread().getName());
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mBookManager = bookManager;
            try {
                bookManager.addNewBookArrivedListener(mINewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            List<Book> bookList;
            try {
                bookList = bookManager.getBookList();
                Log.d(TAG, "onServiceConnected: bookList: "+bookList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                bookManager.addBook(new Book(0x0100, "唐璜"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                bookList = bookManager.getBookList();
                Log.d(TAG, "onServiceConnected: bookList: "+bookList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBookManager = null;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onDestroy() {
        removeNewBookArrivedListener();

        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private void removeNewBookArrivedListener() {
        if (mBookManager == null || !mBookManager.asBinder().isBinderAlive()) {
            Log.e(TAG, "removeNewBookArrivedListener: mBookManager: "+mBookManager);
            return;
        }
        try {
            mBookManager.removeNewBookArrivedListener(mINewBookArrivedListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private INewBookArrivedListener mINewBookArrivedListener = new INewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.d(TAG, "onNewBookArrived: ThreadName: "+Thread.currentThread().getName());
            Log.d(TAG, "onNewBookArrived: book: "+book);
        }
    };

    public void toService2(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
