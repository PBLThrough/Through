package jm.through.navigation

import android.os.Bundle
import android.os.UserManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import jm.through.R
import jm.through.activity.MailActivity
import jm.through.read.UserAdapter
import jm.through.read.UserData
import kotlinx.android.synthetic.main.nav_header_mail.*
import kotlinx.android.synthetic.main.nav_user_fragment.*

class UserRecycler : Fragment(), View.OnClickListener {


    val userList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(userList.size == 0) {
            userList.add(UserData(R.drawable.profile, "dream7739@naver.com", 55))
            userList.add(UserData(R.drawable.profile, "dream7739@gmail.com", 30))
            userList.add(UserData(R.drawable.profile, "dream7739@daum.com", 30))
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.nav_user_fragment, container, false)
        val userAddBtn = v.findViewById(R.id.user_add_btn) as ImageButton

        userAddBtn.setOnClickListener {
        //계정 추가 창으로 이동 & 드로어 닫기    
        }

        val recycler = v.findViewById(R.id.user_recycler) as RecyclerView
        val uAdapter = UserAdapter(userList)

        recycler.adapter = uAdapter
        uAdapter.setOnItemClickListener(this@UserRecycler)
        recycler.layoutManager = LinearLayoutManager(context)

        return v
    }

    override fun onClick(v: View?) {
        val position = user_recycler.getChildAdapterPosition(v)
        val selectEmail = userList.get(position).userEmail
        (activity as MailActivity).selectEmail(selectEmail)

    }

}