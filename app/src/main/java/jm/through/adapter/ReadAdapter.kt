package jm.through.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import jm.through.activity.MailActivity
import jm.through.data.ReadData
import jm.through.viewholder.ReadViewHolder
import java.text.SimpleDateFormat
import java.util.*


/**
 * ReadAdapter <- ReadData
 * */
class ReadAdapter(var dataList: ArrayList<ReadData>): RecyclerView.Adapter<ReadViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event
    private var isMoreLoading = false

    // 현재 시각
    val currentTime = Date()
    val mSimpleDateFormat = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
    val hSimpleDateFormat = SimpleDateFormat("aa hh:mm", Locale.KOREA)
    val mTime = mSimpleDateFormat.format(currentTime)

    override fun onBindViewHolder(holder: ReadViewHolder, position: Int) {
        // ReadDate 요소 가져오기
        var title:String=dataList!!.get(position).mailTitle
        var memo:String=dataList!!.get(position).mailMemo
        var dates:Date? =dataList!!.get(position).mailDate

        holder!!.mailSender.text = title.split("<")[0] // 발신자
        holder!!.mailSubject.text = memo // 내용


        // 메일 날짜가 오늘일 때 오전, 오후 표시
        if (dates.toString() != "null"){
            if ( mSimpleDateFormat.format(dates) == mTime){
                holder!!.mailDate.text = hSimpleDateFormat.format(dates);
           }
            else
                holder!!.mailDate.text = mSimpleDateFormat.format(dates);
        }
        else
            holder!!.mailDate.text = "정보 없음"

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadViewHolder {
        val mainView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.check_board_item,parent,false)
        mainView.setOnClickListener(onItemClick)
        return ReadViewHolder(mainView)
    }

    override fun getItemCount(): Int = dataList.size

    fun setOnItemClickListener(l: MailActivity) {
        onItemClick = l
    }


    fun addMoreItems(location:Int, newItems:ArrayList<ReadData>){
        this.dataList.addAll(location, newItems)
        notifyDataSetChanged()
    }

}