package jm.through.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import jm.through.R


class TrustViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var userEmail: TextView = itemView!!.findViewById(R.id.user_email) as TextView
    var userEmailCount: TextView = itemView!!.findViewById(R.id.user_email_count) as TextView

}