package jm.through.form

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.attachment.AttachViewHolder
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class SchoolAdapter(var dataList: ArrayList<SchoolData>): RecyclerView.Adapter<SchoolViewHolder>() {

    override fun onBindViewHolder(holder: SchoolViewHolder?, position: Int) {
        holder!!.formImage.setImageResource(dataList.get(position).formImg)
        holder!!.formName.text = dataList.get(position).formName
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SchoolViewHolder {
        val schoolView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.form_item,parent,false)
        return SchoolViewHolder(schoolView)

    }

    override fun getItemCount(): Int = dataList.size


}