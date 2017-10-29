package com.houtrry.aidlsamples.impl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.houtrry.aidlsamples.aidl.Book;

import java.util.List;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }
        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if (iInterface != null && iInterface instanceof IBookManager) {
            return (IBookManager) binder;
        }
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

        switch (code) {
            case INTERFACE_TRANSACTION:{
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACT_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> bookList = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(bookList);
                return true;
            }
            case TRANSACT_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if (0!=data.readInt()) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.addBook(book);
                reply.writeNoException();
                return true;
            }
            case TRANSACT_addNewBookArrivedListener: {
                data.enforceInterface(DESCRIPTOR);
                INewBookArrivedListener listener;
                if (0 != data.readInt()) {
                    listener = NewBookArrivedListener.asInterface(data.readStrongBinder());
                } else {
                    listener = null;
                }
                boolean result = this.addNewBookArrivedListener(listener);
                reply.writeNoException();
                reply.writeInt(result?1:0);
                return true;
            }
            case TRANSACT_removeNewBookArrivedListener: {
                data.enforceInterface(DESCRIPTOR);
                INewBookArrivedListener listener;
                if (0 != data.readInt()) {
                    listener = NewBookArrivedListener.asInterface(data.readStrongBinder());
                } else {
                    listener = null;
                }
                boolean result = this.removeNewBookArrivedListener(listener);
                reply.writeNoException();
                reply.writeInt(result?1:0);
                return true;
            }
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public boolean addNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
        return false;
    }

    @Override
    public boolean removeNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
        return false;
    }

    private static class Proxy implements IBookManager {

        private IBinder remoteBinder;

        public Proxy(IBinder remoteBinder) {
            this.remoteBinder = remoteBinder;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public IBinder asBinder() {
            return remoteBinder;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result = null;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                remoteBinder.transact(TRANSACT_getBookList, data, reply, 0);
                reply.writeNoException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();

            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                remoteBinder.transact(TRANSACT_addBook, data, reply, 0);
                reply.writeNoException();
            } finally {
                reply.recycle();
                data.recycle();
            }

        }

        @Override
        public boolean addNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean result = false;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(newBookArrivedListener != null?newBookArrivedListener.asBinder():null);
                remoteBinder.transact(TRANSACT_addNewBookArrivedListener, data, reply, 0);
                reply.writeNoException();

                result = reply.readInt() != 0;
            } finally {
                reply.recycle();
                data.recycle();
            }


            return result;
        }

        @Override
        public boolean removeNewBookArrivedListener(INewBookArrivedListener newBookArrivedListener) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean result = false;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                data.writeStrongBinder(newBookArrivedListener != null?newBookArrivedListener.asBinder():null);
                remoteBinder.transact(TRANSACT_removeNewBookArrivedListener, data, reply, 0);
                reply.writeNoException();
                result = reply.readInt() != 0;
            } finally {
                reply.recycle();
                data.recycle();
            }


            return result;
        }
    }
}
