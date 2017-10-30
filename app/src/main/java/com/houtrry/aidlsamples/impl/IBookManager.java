package com.houtrry.aidlsamples.impl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;


import java.util.List;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public interface IBookManager extends IInterface {

    String DESCRIPTOR = "com.houtrry.aidlsamples.impl.IBookManager";

    int TRANSACT_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    int TRANSACT_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
    int TRANSACT_addNewBookArrivedListener = IBinder.FIRST_CALL_TRANSACTION + 2;
    int TRANSACT_removeNewBookArrivedListener = IBinder.FIRST_CALL_TRANSACTION + 3;

    List<Book> getBookList() throws RemoteException;

    void addBook(Book book) throws RemoteException;

    boolean addNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException;

    boolean removeNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException;
}
