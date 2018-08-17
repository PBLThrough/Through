package jm.through.read

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.attachment.AttachViewHolder
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.internet.MimeUtility
import android.text.InputFilter
import android.widget.ImageView


/**
 * ReadAdapter <- ReadData
 * */
class AttachAdapter(var dataList: ArrayList<AttachData>): RecyclerView.Adapter<AttachViewHolder>() {



    override fun onBindViewHolder(holder: AttachViewHolder?, position: Int) {
        //file type & x버튼 처리는 나중에


        holder!!.fileName.text = dataList!!.get(position).fileName // 이름
        holder!!.fileSize.text = dataList!!.get(position).fileSize.toString() //사이즈



        //확장자 명에 따른 이미지 변경
        var type = dataList!!.get(position).fileType

        if ( type == "jpg") {
            holder!!.typeImage.setImageResource(R.drawable.sign)
        }

        holder!!.deleteImage.setOnClickListener {
            dataList!!.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AttachViewHolder {
        val attachView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.attach_item,parent,false)
        return AttachViewHolder(attachView)
    }


    override fun getItemCount(): Int = dataList.size




}