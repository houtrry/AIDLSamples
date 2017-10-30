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

import com.houtrry.aidlsamples.impl.Book;
import com.houtrry.aidlsamples.impl.BookManagerImpl;
import com.houtrry.aidlsamples.impl.IBookManager;
import com.houtrry.aidlsamples.impl.NewBookArrivedListener;

import java.util.List;

/**
 * @author: houtrry
 */
public class Main2Activity extends AppCompatActivity {

    private static final String TAG = Main2Activity.class.getSimpleName();
    private IBookManager mIBookManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "onCreate: Main2Activity");
        Intent intent = new Intent(this, BookManagerRemoteService2.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unregisterListener();
        Log.d(TAG, "onCreate: Main2Activity");
        super.onDestroy();
    }

    private void unregisterListener() {
        if (mIBookManager != null && mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.removeNewBookArrivedListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = BookManagerImpl.asInterface(service);
            mIBookManager = bookManager;
            Log.d(TAG, "onServiceConnected: bookManager: "+bookManager+", service: "+service+", mNewBookArrivedListener: "+mNewBookArrivedListener);
            Log.d(TAG, "onServiceConnected: mNewBookArrivedListener: "+mNewBookArrivedListener);
            try {
                bookManager.addNewBookArrivedListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            List<Book> bookList;
            try {
                bookList = bookManager.getBookList();
                Log.d(TAG, "onServiceConnected: bookList: "+bookList);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "onServiceConnected: e: "+e);
            }

            try {
                bookManager.addBook(new Book(0x002001, "一九八四"));
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
            mIBookManager = null;
        }
    };



    private NewBookArrivedListener mNewBookArrivedListener = new NewBookArrivedListener() {
        @Override
        public void onNewBookArrived(Book book) {
            super.onNewBookArrived(book);
            Log.d(TAG, "onNewBookArrived: "+book);
        }
    };
}
