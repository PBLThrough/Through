package jm.through.attachment

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
class AttachAdapter(var context: Context, var dataList: ArrayList<AttachData>) : RecyclerView.Adapter<AttachViewHolder>() {
    override fun onBindViewHolder(holder: AttachViewHolder, position: Int) {
        holder!!.fileName.text = dataList!!.get(position).fileName // 이름

        var size: Long = dataList!!.get(position).fileSize
        var formattedSize = formatFileSize(size)
        holder.fileSize.text = formattedSize


        //확장자 명에 따른 이미지 변경
        var type = dataList!!.get(position).fileType
        when (type) {
            "jpg" -> holder!!.typeImage.setImageResource(R.drawable.sign)
            "ppt" -> holder!!.typeImage.setImageResource(R.drawable.profile)
        }

        holder!!.deleteImage.setOnClickListener {
            Log.v("deletePosition", position.toString())
            dataList!!.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged() //dataset이 변경된 걸 알려줌
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachViewHolder {
        val attachView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.attach_item, parent, false)
        return AttachViewHolder(attachView)
    }

    override fun getItemCount(): Int = dataList.size

    fun formatFileSize(bytes: Long): String {
        return android.text.format.Formatter.formatFileSize(context, bytes)
        //formatter로 size를 KB, MB, GB로 바꿔줌
    }
}