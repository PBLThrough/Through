package jm.through.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.GravityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import android.view.inputmethod.InputMethodManager
import jm.through.AccountData
import jm.through.AccountData.accountList
import jm.through.AccountData.selectedData
import jm.through.R
import jm.through.adapter.ReadAdapter
import jm.through.function.FolderFetchImap
import jm.through.function.FolderFetchImap.callMoreMails
import jm.through.read.*
import jm.through.function.FolderFetchImap.readList
import kotlinx.android.synthetic.main.fragment_check.*
import kotlinx.android.synthetic.main.nav_header_mail.*
import kotlin.collections.ArrayList


class MailActivity : AppCompatActivity(), View.OnClickListener {
    var list_lastitemcheck = false;

    var click = true
    val context = this
    lateinit var uAdapter: UserAdapter
    lateinit var rAdapter: ReadAdapter
    var readId = ""
    var readPass = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        //toolbar에 대한 설정
        toolbarSetting()

        //navigation에 대한 설정
        navSetting()


        //헤더 뷰 클릭하면 버튼 회전 & 리사이클러뷰 변경
        header_layout.setOnClickListener {
            animateMenu()
        }


        //플로팅 버튼 누르면 메일 쓰기 화면으로
        send_fab.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }


        //계정 추가레이아웃 클릭 시 계정 추가 화면으로
        val v = findViewById(R.id.add_layout)
        v.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        //TODO 환경설정에서 계정관리 가능
        settingBtn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

            readSetting()


    }

    private fun readEmail() {
        val swipeRefresh = findViewById(R.id.swipeRefresh) as SwipeRefreshLayout

        if (read_progress.visibility == View.INVISIBLE) {
            read_progress.visibility = View.VISIBLE
        }

        //스레드 & 핸들러, mHandler.post이후에 UI작업
        val mHandler = Handler()
        val t = Thread(Runnable {

            var reader = FolderFetchImap()
            reader.setIndex(readId)
            Log.v("정보", readId + readPass)

            reader.readImapMail(readId, readPass)

            mHandler.post {
                try {
                    Log.v("listlist", readList.toString())
                    rAdapter = ReadAdapter(readList)
                    rAdapter.notifyDataSetChanged()
                    rAdapter.setOnItemClickListener(context)

                    recycler.adapter = rAdapter
                    recycler.layoutManager = LinearLayoutManager(context)

                    swipeRefresh.setOnRefreshListener { this }

                    /** 여기에 리사이클러뷰 .addOnScrollListener 추가 */

                    read_progress.visibility = View.INVISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.v("fail", "")
                }
            }
        })
        t.start()

    }

    /** 밑으로 스크롤 시 메일 20개 추가 */
    fun onLoadMore(){
        read_progress.visibility = View.VISIBLE
        //callMoreMails = true;
        //readEmail();
    }

    /** 뷰 클릭 이벤트 **/
    override fun onClick(v: View?) {
        when (v?.parent) {

            //메인 리사이클러뷰, 메일 리스트 항목 클릭 시 상세보기
            recycler -> {
                val idx: Int = recycler.getChildAdapterPosition(v!!)
                val messageIntent = Intent(this.context, MessageActivity::class.java)
                messageIntent.putExtra("position", idx)
                startActivity(messageIntent)
            }

            //계정 선택 리사이클러뷰
            user_recycler -> {
                val idx = user_recycler.getChildAdapterPosition(v!!)
                val data = AccountData.accountList.get(idx)
                selectedData = data
                readId = selectedData!!.id
                readPass = selectedData!!.pass

                //View에 표시, 메인 아이디는 크게, 서브는 작게
                var str = readId.split("@")
                email_main_text.text = str[0]
                email_sub_text.text = "@" + str[1]
                animateMenu()
            }

            //메일 확인 리사이클러뷰
            nav_recycler -> {
                val idx = nav_recycler.getChildAdapterPosition(v!!)
                when (idx) {
                    //메일 읽어오기
                    0 -> {
                        Log.v("hihihi", "눌렷음")
                        readEmail()
                    }

                }
            }


        }


    }



    fun animateMenu() {
        ObjectAnimator.ofFloat(spin_btn, "rotation", if (click) 180f else 0f).start()

        if (click) {
            user_recycler.visibility = View.VISIBLE
            add_layout.visibility = View.VISIBLE
            nav_recycler.visibility = View.GONE
        } else {
            user_recycler.visibility = View.GONE
            add_layout.visibility = View.GONE
            nav_recycler.visibility = View.VISIBLE
        }
        click = !click
    }

    fun readSetting() {
        selectedData = null

        if (accountList.isEmpty()) {
            email_sub_text.visibility = View.GONE
        } else {

            email_sub_text.visibility = View.VISIBLE

            //계정리스트가 null이 아니지만 선택된 data가 없을 때, 리스트 마지막 값으로 read
            if (selectedData == null) {

                selectedData = accountList.last()
                readId = selectedData!!.id
                readPass = selectedData!!.pass

                email_main_text.text = readId.split("@")[0]
                email_sub_text.text = "@" + readId.split("@")[1]
                readEmail()

            }
        }
    }

    fun navSetting() {
        //드로어 첫 화면

        val navList = ArrayList<NavData>()

        if (navList.size == 0) {
            navList.add(NavData(R.drawable.read, "받은 메일함", 99, R.drawable.write, null))
            navList.add(NavData(R.drawable.write, "보낸 메일함", 33, R.drawable.write, null))
        }
        val nAdapter = NavAdapter(navList)
        nav_recycler.adapter = nAdapter
        nAdapter.setOnItemClickListener(this@MailActivity)
        nav_recycler.layoutManager = LinearLayoutManager(this)

        uAdapter = UserAdapter(accountList)
        user_recycler.adapter = uAdapter
        uAdapter.setOnItemClickListener(this@MailActivity)
        user_recycler.layoutManager = LinearLayoutManager(this)

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
        //val fm = fragmentManager;
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }



    inner class ReadTask : AsyncTask<Void, Void, Void>() {

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            read_progress.visibility = View.INVISIBLE
            try {
                Log.v("listlist",readList.toString());
                rAdapter = ReadAdapter(readList)
                rAdapter.notifyDataSetChanged()
                rAdapter.setOnItemClickListener(context)
                recycler.adapter = rAdapter
                recycler.layoutManager = LinearLayoutManager(context)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("fail", "")
            }
        }

        override fun doInBackground(vararg params: Void?): Void? {
            System.out.println("mailActivity-background loading..");
            return null
        }


        override fun onPreExecute() {
            super.onPreExecute()
            readList.clear()
            if (read_progress.visibility == View.INVISIBLE) {
                read_progress.visibility = View.VISIBLE
            }
        }

    }

    //onStop상태에서 돌아올 때, onRestart->onStart-> onResume
    override fun onRestart() {
        super.onRestart()
        Log.v("onRestart", "resume")
    }


    //사용자에게 보여지기 직전
    override fun onStart() {
        super.onStart()
        Log.v("onStart", "resume")

    }


    //사용자와 상호작용하기 직전 (pause -> resume)
    override fun onResume() {
        super.onResume()

        //계정 추가 후 돌아왔을 때 accountList의 가장 마지막 계정으로 메일 읽기
        readSetting()

    }

    override fun onPause() {
        super.onPause()
        Log.v("onPause", "resume")
    }

    override fun onStop() {
        super.onStop()
        drawer_layout.closeDrawer(GravityCompat.START)
        Log.v("onStop", "resume")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.v("onDestroy", "resume")

    }

}




