package com.larry.testaidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author larryycliu on 12/1/21.
 */
public class RemoteService extends Service {

    private List<Book> bookList = new LinkedList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        bookList.clear();
        Log.w("lllll", "onBind()");
        return mBind;
    }

    private final IBookManager.Stub mBind = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.w("lllll", "getBookList():" + bookList.size());
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.w("lllll", "addBook():" + book.id);
            if (book != null) {
                bookList.add(book);
            }
        }
    };
}
