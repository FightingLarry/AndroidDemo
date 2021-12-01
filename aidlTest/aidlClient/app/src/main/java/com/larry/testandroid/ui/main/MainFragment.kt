package com.larry.testandroid.ui.main

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.larry.testaidlservice.Book
import com.larry.testaidlservice.IBookManager
import com.larry.testandroid.R
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.lang.StringBuilder

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var addView: Button
    private lateinit var getBooksView: Button
    private lateinit var booksView: TextView

    private var bookManager: IBookManager? = null

    private val conn: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bookManager = IBookManager.Stub.asInterface(service)
            Log.i("lllll", "client:onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bookManager = null
            Log.i("lllll", "client:onServiceDisconnected")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent("com.larry.testaidlservice.RemoteService")
        intent.setPackage("com.larry.testaidlservice")
        activity?.bindService(intent, conn, Activity.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unbindService(conn)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val parent = inflater.inflate(R.layout.main_fragment, container, false)
        addView = parent.findViewById(R.id.add)
        getBooksView = parent.findViewById(R.id.get)
        booksView = parent.findViewById(R.id.bookList)

        addView.setOnClickListener {
            val book = Book()
            book.id = View.generateViewId()
            bookManager?.addBook(book)
        }
        getBooksView.setOnClickListener {
            val list = bookManager?.bookList
            if (list != null) {
                val sb = StringBuilder("获取的书籍：\n")
                for (book in list) {
                    sb.append(book.id).append('\n')
                }
                booksView.text = sb.toString()
            }

        }
        return parent
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

    }


}
