package jm.through.read

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jm.through.R
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.internet.MimeUtility

class ReadAdapter(var dataList: ArrayList<ReadData>): RecyclerView.Adapter<ReadViewHolder>() {
    private var onItemClick: View.OnClickListener? = null //item클릭 시 event
    // 현재 시각
    val currentTime = Date()
    val mSimpleDateFormat = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
    val mTime = mSimpleDateFormat.format(currentTime)

    override fun onBindViewHolder(holder: ReadViewHolder?, position: Int) {
        // ReadDate 요소 가져오기
        var title:String=dataList!!.get(position).mailTitle
        var content:String=dataList!!.get(position).mailContent
        var dates:String =dataList!!.get(position).mailDate
//        var check:Boolean=dataList!!.get(position).check

        // ReadViewHolder의 text를 가져온 ReadData 로 채우기
        holder!!.mailSender.text = title.split("<")[0] // 발신자
        holder!!.mailSubject.text = content // 내용
        println("currentdate = "+currentTime  );


        if (dates != "null") {
            var returnValue = dates.split(" ")[3];
            var timeset = Integer.parseInt(returnValue.substring(0,2)); // 오전, 오후로 나눌 시간
            var behinddates = returnValue.substring(3,5);

            // 오늘일 경우
            if (dates.toString().substring(0,9) == currentTime.toString().substring(0,9)){
                // 오전, 오후
                if (timeset >= 12 && timeset <=24) {
                    timeset -= 12;
                    returnValue = "오후 " + timeset +":"+ behinddates;
                }
                else if (timeset >=0){
                    returnValue = "오전 " + timeset +":"+ behinddates;
                }
                holder!!.mailDate.text = returnValue
            }
            else {
                holder!!.mailDate.text = mTime
            }
        } else {
            // date = "null"
            holder!!.mailDate.text = "정보 없음"
        }

//        if(check)
//        holder!!.checkImg.setBackgroundResource(R.drawable.make_checkbox_on)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReadViewHolder {
        val mainView:View=LayoutInflater.from(parent!!.context).inflate(R.layout.check_board_item,parent,false)
        mainView.setOnClickListener(onItemClick)
        return ReadViewHolder(mainView)
    }



    override fun getItemCount(): Int = dataList.size

    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }
}