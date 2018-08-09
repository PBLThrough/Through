package jm.through.read

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R

class ReadAdapter(var dataList: ArrayList<ReadData>): RecyclerView.Adapter<ReadViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event
    override fun onBindViewHolder(holder: ReadViewHolder?, position: Int) {
        var title:String=dataList!!.get(position).mailTitle
        var content:String=dataList!!.get(position).mailContent
        var check:Boolean=dataList!!.get(position).check

        holder!!.mailSender.text=title
        holder!!.mailSubject.text=content

        if(check)
        holder!!.checkImg.setBackgroundResource(R.drawable.make_checkbox_on)
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReadViewHolder {
        val mainView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.check_board_item,parent,false)
        mainView.setOnClickListener(onItemClick)
        return ReadViewHolder(mainView)
    }



    override fun getItemCount(): Int = dataList.size

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }
}