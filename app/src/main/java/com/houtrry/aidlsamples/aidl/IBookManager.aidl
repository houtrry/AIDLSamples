// IBookManager.aidl
package com.houtrry.aidlsamples.aidl;

import com.houtrry.aidlsamples.aidl.Book;
import com.houtrry.aidlsamples.aidl.INewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    boolean addNewBookArrivedListener(in INewBookArrivedListener newBookArrivedListener);
    boolean removeNewBookArrivedListener(in INewBookArrivedListener newBookArrivedListener);
}
