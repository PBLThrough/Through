package jm.through.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.AccountData
import jm.through.R
import jm.through.activity.SendActivity
import jm.through.adapter.SendBarAdapter
import jm.through.activity.MainActivity



class SendBarFragment: Fragment(), View.OnClickListener {
    lateinit var sendbar_recycler:RecyclerView
    lateinit var adapter:SendBarAdapter

    override fun onClick(v: View?) {
        Log.v("hihihihi","hi")
        val position = sendbar_recycler.getChildAdapterPosition(v)
        (context as SendActivity).setSender(position)
        (context as SendActivity).closeRecycler()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.sendbar_fragment, container, false)
        sendbar_recycler = v.findViewById(R.id.sendbar_recycler) as RecyclerView
        adapter = SendBarAdapter(context, AccountData.accountList)
        sendbar_recycler.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListenr(this@SendBarFragment)
        sendbar_recycler.adapter = adapter
        return  v

    }
}