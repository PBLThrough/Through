package jm.through.account

import android.accounts.Account
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.activity.MailActivity

class AccountAdapter(var dataList: ArrayList<PlatformData>) : RecyclerView.Adapter<AccountViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val accountView = LayoutInflater.from(parent!!.context).inflate(R.layout.account_item, parent, false)
        accountView.setOnClickListener(onItemClick)
        return AccountViewHolder(accountView)
    }


    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.platformImage.setImageResource(dataList.get(position).platformImg)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    fun setOnItemClickListener(l: AccountActivity) {
        onItemClick = l
    }
}