package jm.through.attachment


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class RattachAdapter(var context: Context, var dataList: ArrayList<RattachData>): RecyclerView.Adapter<RattachViewHolder>() {
    override fun onBindViewHolder(holder: RattachViewHolder, position: Int) {
        //file type & x버튼 처리는 나중에


        holder!!.receive_fileName.text = dataList!!.get(position).receive_fileName // 이름

        var size:Long = dataList!!.get(position).receive_fileSize
        var formattedSize = formatFileSize(size)
        holder.receive_fileSize.text = formattedSize


//        //확장자 명에 따른 이미지 변경
//        var type = dataList!!.get(position).receive_fileType
//        when (type) {
//            "jpg" -> holder!!.typeImage.setImageResource(R.drawable.sign)
//            "ppt" -> holder!!.typeImage.setImageResource(R.drawable.profile)
//        }

        holder!!.receive_loadImage.setOnClickListener {
            Log.v("deletePosition",position.toString())
            dataList!!.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged() //dataset이 변경된 걸 알려줌
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RattachViewHolder {
        val RattachView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.rattach_item,parent,false)
        return RattachViewHolder(RattachView)

    }

    override fun getItemCount(): Int = dataList.size

    fun  formatFileSize(bytes: Long) : String{
        return android.text.format.Formatter.formatFileSize(context, bytes)
        //formatter로 size를 KB, MB, GB로 바꿔줌
    }
}