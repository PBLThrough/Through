package jm.through.read

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.attachment.NavViewHolder
import jm.through.attachment.UserViewHolder
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class UserAdapter(var dataList: ArrayList<UserData>): RecyclerView.Adapter<UserViewHolder>() {
    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder!!.userImg.setImageResource(dataList!!.get(position).userImg)
        holder!!.userEmail.text = dataList!!.get(position).userEmail // 이름
        holder!!.userEmailCount.text = dataList!!.get(position).userEmailCount.toString()
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder {
        val userView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.nav_user_item,parent,false)
        return UserViewHolder(userView)
    }

    override fun getItemCount(): Int = dataList.size

}