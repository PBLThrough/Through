package jm.through.attachment

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R


class RattachViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    var receive_typeImage: ImageView =itemView!!.findViewById(R.id.receive_type_image) as ImageView
    var receive_fileName:TextView=itemView!!.findViewById(R.id.receive_file_name) as TextView
    var receive_fileSize:TextView = itemView!!.findViewById(R.id.receive_file_size) as TextView
    var receive_loadImage:ImageView= itemView!!.findViewById(R.id.receive_load_image)as ImageView

}
