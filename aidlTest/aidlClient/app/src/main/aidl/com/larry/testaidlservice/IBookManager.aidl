// IBookManager.aidl
package com.larry.testaidlservice;
import com.larry.testaidlservice.Book;
import java.util.List;

interface IBookManager{

    List<Book> getBookList();

    void addBook(in Book book);

}
