package com.houtrry.aidlsamples.impl;

import android.os.Binder;
import android.os.IInterface;


/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public interface INewBookArrivedListener extends IInterface {

    String DESCRIPTOR = "com.houtrry.aidlsamples.impl.INewBookArrivedListener";

    int Transact_onNewBookArrived = Binder.FIRST_CALL_TRANSACTION + 0;

    void onNewBookArrived(Book book);
}
