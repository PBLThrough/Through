package jm.through.attachment

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R


class UserViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {


    var userImg: ImageView =itemView!!.findViewById(R.id.user_img) as ImageView
    var userEmail:TextView=itemView!!.findViewById(R.id.user_email) as TextView
    var userEmailCount:TextView = itemView!!.findViewById(R.id.user_email_count) as TextView

}

