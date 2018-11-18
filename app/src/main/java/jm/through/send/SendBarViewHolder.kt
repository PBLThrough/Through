package jm.through.send

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import jm.through.R

class SendBarViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
    var email_text: TextView = itemView!!.findViewById(R.id.email_text) as TextView
}