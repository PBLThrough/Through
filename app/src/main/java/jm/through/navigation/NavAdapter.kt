package jm.through.read

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.attachment.NavViewHolder
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class NavAdapter(var dataList: ArrayList<NavData>): RecyclerView.Adapter<NavViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {

        holder!!.navName.text = dataList!!.get(position).navName // 이름
        holder!!.navClean.setImageResource(dataList!!.get(position).navClean) //휴지통
        holder!!.navCount.text = dataList!!.get(position).navCount.toString() //메일 개수
        holder!!.navImg.setImageResource(dataList!!.get(position).navImg) //앞에 보이는 아이콘


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val navView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.nav_list_item,parent,false)
        navView.setOnClickListener(onItemClick)
        return NavViewHolder(navView)
    }


    override fun getItemCount(): Int = dataList.size

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

}