package jm.through.read

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import jm.through.R

/**
 * ReadViewHolder <- check_board_item
 * */
class ReadViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    // ReadViewHolder 의 구성요소 선언과 check_board_item 레이아웃 구성요소 연결시키기
    var mailSender: TextView =itemView!!.findViewById(R.id.text_sender) as TextView // 레이아웃에서 가져옴
    var mailSubject:TextView=itemView!!.findViewById(R.id.text_subject) as TextView
   // var checkImg:ImageView=itemView!!.findViewById(R.id.image_check) as ImageView
    var mailDate:TextView= itemView!!.findViewById(R.id.text_time)as TextView
}