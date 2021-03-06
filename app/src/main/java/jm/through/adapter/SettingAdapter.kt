package jm.through.adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.activity.SettingActivity
import jm.through.data.DetailData
import jm.through.fragment.DeleteDialogFragment
import jm.through.fragment.SendBarFragment
import jm.through.fragment.TrustDialogFragment
import jm.through.viewholder.SendBarViewHolder
import jm.through.viewholder.SettingViewHolder

class SettingAdapter(var context: Context, var dataList:ArrayList<DetailData>): RecyclerView.Adapter<SettingViewHolder>() {

    private var onItemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingViewHolder {
        val settingView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.setting_item,parent,false)
        settingView.setOnClickListener(onItemClick)
        return SettingViewHolder(settingView)
    }


    override fun onBindViewHolder(holder: SettingViewHolder?, position: Int) {
        holder!!.setting_text.text = dataList.get(position).id

        //x버튼 누르면 삭제
        holder.setting_delete_btn.setOnClickListener{
            val dialog = DeleteDialogFragment()
            var bundle = Bundle()
            bundle.putInt("position",position)
            dialog.arguments = bundle
            dialog.show((context as SettingActivity).supportFragmentManager, "삭제 다이얼로그")
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setOnItemClickListenr(l: SettingActivity){
        onItemClick = l
    }


}