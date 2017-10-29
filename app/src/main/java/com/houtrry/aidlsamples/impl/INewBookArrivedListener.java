package com.houtrry.aidlsamples.impl;

import android.os.Binder;
import android.os.IInterface;

import com.houtrry.aidlsamples.aidl.Book;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public interface INewBookArrivedListener extends IInterface {

    static final String DESCRIPTOR = "com.houtrry.aidlsamples.impl.INewBookArrivedListener";

    static final int Transact_onNewBookArrived = Binder.FIRST_CALL_TRANSACTION + 0;

    void onNewBookArrived(Book book);
}
