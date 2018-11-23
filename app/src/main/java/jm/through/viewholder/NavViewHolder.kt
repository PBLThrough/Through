package jm.through.attachment

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R


class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    var navImg: ImageView =itemView!!.findViewById(R.id.menu_icon) as ImageView
    var navName:TextView=itemView!!.findViewById(R.id.menu_text) as TextView
    var navCount:TextView = itemView!!.findViewById(R.id.count_text) as TextView
    var navClean:ImageView= itemView!!.findViewById(R.id.clean_btn)as ImageView

}

