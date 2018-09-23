package jm.through.read

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.AccountData
import jm.through.account.DetailData
import jm.through.R
import jm.through.navigation.UserViewHolder
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class UserAdapter(var accountList: ArrayList<DetailData>) : RecyclerView.Adapter<UserViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Log.v("accountList2", accountList.toString())
        var detail = accountList.get(position)
        holder!!.userEmail.text = detail.id
        if (detail.count >= 100) {
            holder!!.userEmailCount.text = "99+"
        } else {
            holder!!.userEmailCount.text = detail.count.toString()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        Log.v("hiyo..", "hihi")

        val userView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.nav_user_item, parent, false)
        userView.setOnClickListener(onItemClick)

        return UserViewHolder(userView)
    }

    override fun getItemCount(): Int = AccountData.accountList.size

    //setter 리스너를 세팅해주는 부분
    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }
}