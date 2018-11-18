package jm.through.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.AccountData
import jm.through.R
import jm.through.adapter.SendBarAdapter

class SendBarFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.sendbar_fragment, container, false)
        val sendbar_recycler = v.findViewById(R.id.sendbar_recycler) as RecyclerView
        sendbar_recycler.layoutManager = LinearLayoutManager(context)
        sendbar_recycler.adapter = SendBarAdapter(context, AccountData.accountList)
        return  v

    }
}