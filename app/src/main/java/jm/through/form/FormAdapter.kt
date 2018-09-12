package jm.through.form

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class FormAdapter(var dataList: ArrayList<FormData>): RecyclerView.Adapter<FormViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event

    override fun onBindViewHolder(holder: FormViewHolder, position: Int) {
        holder!!.formImage.setImageResource(dataList.get(position).formImg)
        holder!!.formName.text = dataList.get(position).formName
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormViewHolder {
        val formView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.form_item,parent,false)
        formView.setOnClickListener(onItemClick)
        return FormViewHolder(formView)
    }

    override fun getItemCount(): Int = dataList.size

    fun setOnItemClickListener(l:View.OnClickListener){
        onItemClick = l
    }

}