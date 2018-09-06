package jm.through.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.activity.MailActivity
import jm.through.activity.MainActivity
import jm.through.read.NavAdapter
import jm.through.read.NavData
import jm.through.read.ReadFragment
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.nav_list_fragment.*

class NavRecycler : Fragment(), View.OnClickListener {


    val navList = ArrayList<NavData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(navList.size == 0){
        navList.add(NavData(R.drawable.profile, "메일 확인", 55, R.drawable.sign))
        navList.add(NavData(R.drawable.profile, "받은 메일함", 99, R.drawable.sign))
        navList.add(NavData(R.drawable.profile, "보낸 메일함", 33, R.drawable.sign))
        navList.add(NavData(R.drawable.profile, "휴지통", 30, R.drawable.sign)) }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.nav_list_fragment, container, false)

        val recycler = v.findViewById(R.id.nav_recycler) as RecyclerView
        val nAdapter = NavAdapter(navList)
        recycler.adapter = nAdapter
        nAdapter.setOnItemClickListener(this@NavRecycler)
        recycler.layoutManager = LinearLayoutManager(context)

        return v
    }


    override fun onClick(v: View?) {

        val position = nav_recycler.getChildAdapterPosition(v)

        when(position){
            //0. 전체메일함(다
            // 음에서 전체메일함 있는데 왜 쓰는지 모름), 1. 받은메일함, 2. 보낸메일함, 3. 휴지통
            0 -> {
                val manager = fragmentManager
                val read = ReadFragment()
                manager.beginTransaction().replace(R.id.fragment_container, read).commit()
                (activity as MailActivity).closeDrawer()

            }
        }


    }


}