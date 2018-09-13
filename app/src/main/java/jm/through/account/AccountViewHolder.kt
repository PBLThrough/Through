package jm.through.account

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import jm.through.R

class AccountViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
    var platformImage = itemView!!.findViewById(R.id.platform_img) as ImageView
}