package jm.through.form

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jm.through.R


class FormViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var formImage: ImageView =itemView!!.findViewById(R.id.form_image) as ImageView
    var formName:TextView=itemView!!.findViewById(R.id.form_text) as TextView

}

