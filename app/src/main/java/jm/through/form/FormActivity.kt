package jm.through.form

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.Toast

import jm.through.R
import jm.through.attachment.AttachAdapter
import jm.through.attachment.AttachData
import jm.through.send.SendActivity
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.fragment_send.*

class FormActivity : AppCompatActivity() {
    lateinit var sAdapter: SchoolAdapter //recycler연결시킬 adapte
    var school_list: ArrayList<SchoolData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        school_list.add(SchoolData(R.drawable.form1, "성적문의"))
        school_list.add(SchoolData(R.drawable.form1, "성적문의"))
        school_list.add(SchoolData(R.drawable.form1, "성적문의"))
        school_list.add(SchoolData(R.drawable.form1, "성적문의"))
        school_list.add(SchoolData(R.drawable.form1, "성적문의"))

        sAdapter = SchoolAdapter(school_list)
        school_recycler.adapter = sAdapter
        var manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        school_recycler.layoutManager = manager




        //
        //                Intent intent = new Intent(getApplicationContext(), SendActivity.class);
        //                startActivity(intent);
        //                finish();

    }
}
