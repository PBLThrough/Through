package jm.through.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import jm.through.R



class SettingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var setting_text: TextView = itemView!!.findViewById(R.id.setting_email_text) as TextView
    var setting_delete_btn: ImageButton = itemView!!.findViewById(R.id.setting_delete_btn) as ImageButton
}