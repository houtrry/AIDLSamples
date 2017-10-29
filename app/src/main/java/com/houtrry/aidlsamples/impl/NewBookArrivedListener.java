package com.houtrry.aidlsamples.impl;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.houtrry.aidlsamples.aidl.Book;

/**
 * @author: houtrry
 * @time: 2017/10/29
 * @desc: ${TODO}
 */

public class NewBookArrivedListener extends Binder implements INewBookArrivedListener {

    public NewBookArrivedListener() {
        this.attachInterface(this, DESCRIPTOR);
    }

    @Override
    public IBinder asBinder() {
        return null;
    }

    public static INewBookArrivedListener asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }
        if (binder != null && binder instanceof INewBookArrivedListener) {
            return (INewBookArrivedListener) binder;
        }
        return new Proxy(binder);
    }

    @Override
    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case Transact_onNewBookArrived: {
                data.enforceInterface(DESCRIPTOR);
                Book book;
                if (0 != data.readInt()) {
                    book = Book.CREATOR.createFromParcel(data);
                } else {
                    book = null;
                }
                this.onNewBookArrived(book);
                reply.writeNoException();
                return true;
            }
            default:
                break;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public void onNewBookArrived(Book book) {
        // TODO: 2017/10/29 待实现
    }

    private static class Proxy implements INewBookArrivedListener {

        private IBinder remoteBinder;

        public Proxy(IBinder remoteBinder) {
            this.remoteBinder = remoteBinder;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        public void onNewBookArrived(Book book) {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInterfaceToken(DESCRIPTOR);
            try {
                if (book != null) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                remoteBinder.transact(Transact_onNewBookArrived, data, reply, 0);
                reply.writeNoException();
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return remoteBinder;
        }
    }

}
