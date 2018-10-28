package jm.through.form

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

import jm.through.R
import jm.through.send.SendActivity
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var sAdapter: FormAdapter //recycler연결시킬 adapter
    lateinit var cAdapter: FormAdapter //recycler연결시킬 adapter
    lateinit var uAdapter:FormAdapter
    var school_list: ArrayList<FormData> = ArrayList()
    var company_list: ArrayList<FormData> = ArrayList()
    var custom_list: ArrayList<FormData> = ArrayList()


    //폼 클릭시 이벤트 처리
    override fun onClick(v: View?) {
        val recycler = v!!.parent //item을 갖고 있는 recycler는 item의 부모뷰
        val intent = Intent(applicationContext, SendActivity::class.java)

        when(recycler){
            school_recycler -> {
                val position = school_recycler.getChildAdapterPosition(v)
                intent.putExtra("formDetail", school_list.get(position).formDetail)

            }
            company_recycler -> {
                val position = company_recycler.getChildAdapterPosition(v)
                intent.putExtra("formDetail", company_list.get(position).formDetail)

            }
            custom_recycler -> {
                val position = custom_recycler.getChildAdapterPosition(v)
                intent.putExtra("formDetail", custom_list.get(position).formDetail)
            }
        }

        startActivity(intent)
        finish()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        attachRecycler()
    }

    private fun attachRecycler() {
        //커스텀 -> 서버 데이터로 교체, 기존 폼은 그대로 유지
        school_list.add(FormData(R.drawable.form1, "성적문의","성적1"))
        school_list.add(FormData(R.drawable.form1, "성적문의","성적2"))
        school_list.add(FormData(R.drawable.form1, "성적문의","성적3"))
        school_list.add(FormData(R.drawable.form1, "성적문의","성적4"))
        school_list.add(FormData(R.drawable.form1, "성적문의","성적5"))

        company_list.add(FormData(R.drawable.form1, "견적서문의","견적서1"))
        company_list.add(FormData(R.drawable.form1, "견적서문의","견적서2"))
        company_list.add(FormData(R.drawable.form1, "견적서문의","견적서3"))


        custom_list.add(FormData(R.drawable.form1, "결제메일","커스텀1"))
        custom_list.add(FormData(R.drawable.form1, "게임메일","커스텀2"))
        custom_list.add(FormData(R.drawable.form1, "쇼핑메일","커스텀3"))


        //학교 폼
        var manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        sAdapter = FormAdapter(school_list)
        sAdapter.setOnItemClickListener(this)
        school_recycler.adapter = sAdapter
        school_recycler.layoutManager = manager

        //회사 폼
        var manager2 = LinearLayoutManager(this)
        manager2.orientation = LinearLayoutManager.HORIZONTAL
        cAdapter = FormAdapter(company_list)
        cAdapter.setOnItemClickListener(this)
        company_recycler.adapter = cAdapter
        company_recycler.layoutManager = manager2

        //커스텀 폼
        var manager3 = LinearLayoutManager(this)
        manager3.orientation = LinearLayoutManager.HORIZONTAL
        uAdapter = FormAdapter(custom_list)
        uAdapter.setOnItemClickListener(this)
        custom_recycler.adapter = uAdapter
        custom_recycler.layoutManager = manager3
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SendActivity::class.java))
        finish()
    }
}
