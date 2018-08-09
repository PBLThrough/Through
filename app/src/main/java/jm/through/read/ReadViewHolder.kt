package jm.through.read

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R

class ReadViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var mailSender: TextView =itemView!!.findViewById(R.id.text_sender) as TextView
    var mailSubject:TextView=itemView!!.findViewById(R.id.text_subject) as TextView
    var checkImg:ImageView=itemView!!.findViewById(R.id.image_check) as ImageView

}