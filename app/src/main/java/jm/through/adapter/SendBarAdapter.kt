package jm.through.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.data.DetailData
import jm.through.viewholder.SendBarViewHolder

class SendBarAdapter(var context: Context, var dataList:ArrayList<DetailData>): RecyclerView.Adapter<SendBarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SendBarViewHolder {
        val sendBarItemView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.sendbar_item,parent,false)
        return SendBarViewHolder(sendBarItemView)
    }


    override fun onBindViewHolder(holder: SendBarViewHolder?, position: Int) {
        holder!!.email_text.text = dataList.get(position).id
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}