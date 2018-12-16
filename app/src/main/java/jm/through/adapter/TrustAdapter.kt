package jm.through.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.activity.TrustActivity
import jm.through.data.SignInResult
import jm.through.viewholder.TrustViewHolder

class TrustAdapter(var context: Context, var dataList:ArrayList<SignInResult.EmailData>): RecyclerView.Adapter<TrustViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TrustViewHolder {
        val trustView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.trust_item,parent,false)
        return TrustViewHolder(trustView)
    }


    override fun onBindViewHolder(holder: TrustViewHolder?, position: Int)  {
        holder!!.trustEmail.text = dataList.get(position).email
    }

    override fun getItemCount(): Int {
        return dataList.size
    }




}