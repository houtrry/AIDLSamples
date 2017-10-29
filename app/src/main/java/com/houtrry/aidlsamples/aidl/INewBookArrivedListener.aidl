// INewBookArrivedListener.aidl
package com.houtrry.aidlsamples.aidl;

import com.houtrry.aidlsamples.aidl.Book;
interface INewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
