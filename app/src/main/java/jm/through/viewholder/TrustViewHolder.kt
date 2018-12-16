package jm.through.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import jm.through.R


class TrustViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var trustEmail: TextView = itemView!!.findViewById(R.id.trust_text) as TextView
    var trustDeleteBtn: ImageButton = itemView!!.findViewById(R.id.trust_delete_btn) as ImageButton

}