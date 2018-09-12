package jm.through.attachment

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R


class AttachViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {


    var typeImage: ImageView =itemView!!.findViewById(R.id.type_image) as ImageView
    var fileName:TextView=itemView!!.findViewById(R.id.file_name) as TextView
    var fileSize:TextView = itemView!!.findViewById(R.id.file_size) as TextView
    var deleteImage:ImageView= itemView!!.findViewById(R.id.delete_image)as ImageView

}

