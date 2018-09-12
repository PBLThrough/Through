package jm.through.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import android.view.inputmethod.InputMethodManager
import jm.through.R
import jm.through.attachment.RattachData
import jm.through.read.FolderFetchImap
import jm.through.read.FolderFetchImap.readList
import jm.through.read.NavAdapter
import jm.through.read.NavData
import jm.through.read.ReadAdapter
import jm.through.send.SendActivity
import kotlinx.android.synthetic.main.fragment_check.*
import kotlinx.android.synthetic.main.nav_header_mail.*
import java.io.File


 class MailActivity : AppCompatActivity(), View.OnClickListener {
    var check = true
    val context = this

    lateinit var rAdapter:ReadAdapter
    var rattach_list: ArrayList<RattachData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        toolbarSetting() //toolbar에 대한 설정
        navSetting() //navigation에 대한 설정
        readSetting() //read recycler에 대한 설정


        //플로팅 버튼 누르면 메일 쓰기 화면으로
        send_fab.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onClick(v: View?) {
        val idx: Int = recycler.getChildAdapterPosition(v!!)
        Log.v("idxidx",idx.toString())

//        var bundle = Bundle()
//        bundle.putInt("position", idx)
//
//        var message = MessageFragment() as android.support.v4.app.Fragment//메일 보내는 fragment
//        var fm = fragmentManager //fragment교체에 필요한 fragmentManager
//
//        message.arguments = bundle
//
//        if (fm != null) {
//            fm.beginTransaction().replace(R.id.fragment_container, message).commit()
//        }
    }


    fun readSetting() {
        var readTask = ReadTask()
        readTask.execute()
    }

    fun navSetting() {
        //드로어 첫 화면
        val navList = ArrayList<NavData>()

        if (navList.size == 0) {
            navList.add(NavData(R.drawable.profile, "메일 확인", 55, R.drawable.sign))
            navList.add(NavData(R.drawable.profile, "받은 메일함", 99, R.drawable.sign))
            navList.add(NavData(R.drawable.profile, "보낸 메일함", 33, R.drawable.sign))
            navList.add(NavData(R.drawable.profile, "휴지통", 30, R.drawable.sign))
        }

        val nAdapter = NavAdapter(navList)
        nav_recycler.adapter = nAdapter
        nAdapter.setOnItemClickListener(this@MailActivity)
        nav_recycler.layoutManager = LinearLayoutManager(this)

    }


    fun toolbarSetting() {

        //toolbar를 actionbar처럼 사용
        setSupportActionBar(toolbar)

        //드로어 toolbar toggle에 붙이기
        val toggle = object : ActionBarDrawerToggle(this, drawer_layout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            //드로어가 닫힐 때 키보드를 넣어주는 메소드
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                var imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

            }

            //드로어가 열릴 때도 키보드를 넣어주어야 함.
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                var imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }


        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }


    inner class ReadTask : AsyncTask<Void, Void, Void>() {

         override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            read_progress.visibility = View.INVISIBLE
            try {
                rAdapter = ReadAdapter(readList)
                rAdapter.setOnItemClickListener(context)
                recycler.adapter = rAdapter
                recycler.layoutManager = LinearLayoutManager(context)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("fail", "")
            }
        }

        override fun doInBackground(vararg params: Void?): Void? {
            var reader = FolderFetchImap()
            reader.readImapMail("dream7739", "!hjmh9811387")
            Log.v("list", readList.toString())
            return null
        }

        fun receiveAttach(uri: String) {
            var file: File = File(uri)
            var totalFileSize:Long = 0

            var fileUri = uri
            var fileSize = file.length()
            var fileName = file.name
            var fileType = fileName.split('.')[1]

            for(attachData in rattach_list) {
                totalFileSize += attachData.receive_fileSize //기존 파일 크기
            }
            totalFileSize += fileSize //현재 select해서 가져온 파일 크기

            //gmail 20MB 이상일 시, naver 10MB 이상일 시(상황에 따라 처리 But 속도가 느릴 수 있음.)
            if (totalFileSize  > 10485760) {
                System.out.println("파일첨부 10MB 넘어감")
                //Toast.makeText(this, "파일첨부는 10MB를 넘을 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                rattach_list.add(RattachData(fileUri, fileType, fileName, fileSize))
                Log.v("fileInfo", rattach_list.toString())
                Log.v("totalFileSize", totalFileSize.toString())
            }
        }
        override fun onCancelled(result: Void?) {
            super.onCancelled(result)
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }
    }

}








//    /**선택한 이메일 헤더에 반영*/
//    fun selectEmail(email:String){
//        val str = email.split("@") //0이 아이디, 1이 플랫폼 주소
//        email_main_text.text = str[0]
//        email_sub_text.text = "@"+str[1]
//    }


