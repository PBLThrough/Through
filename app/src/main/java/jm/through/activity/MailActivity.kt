package jm.through.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.sun.mail.imap.IMAPFolder
import jm.through.AccountData
import jm.through.AccountData.accountList
import jm.through.AccountData.selectedData
import jm.through.R
import jm.through.account.AccountActivity
import jm.through.account.AddAccountActivity
import jm.through.attachment.RattachData
import jm.through.read.*
import jm.through.send.SendActivity
import kotlinx.android.synthetic.main.fragment_check.*
import kotlinx.android.synthetic.main.nav_header_mail.*
import java.io.File
import java.util.*
import javax.mail.*
import javax.mail.internet.MimeUtility
import kotlin.collections.ArrayList


class MailActivity : AppCompatActivity(), View.OnClickListener {

    var click = true
    val context = this
    lateinit var uAdapter: UserAdapter
    lateinit var rAdapter: ReadAdapter
    var rattach_list: ArrayList<RattachData> = ArrayList()
    var readId = ""
    var readPass = ""

    companion object Task {
        var readTask = MailActivity().ReadTask()
        var readList = ArrayList<ReadData>()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)

        toolbarSetting() //toolbar에 대한 설정
        navSetting() //navigation에 대한 설정


        //헤더 뷰 클릭하면 버튼 회전 & 리사이클러뷰 변경
        header_layout.setOnClickListener {
            //버튼 1번 클릭시 180도 회전하면서 recyclerview 교체, 클릭은 false로 변경
            animateMenu()

        }


        //플로팅 버튼 누르면 메일 쓰기 화면으로
        send_fab.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }


        //계정 추가레이아웃 클릭 시 계정 추가 화면으로
        val v = findViewById<RelativeLayout>(R.id.add_layout)
        v.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }


    }


    override fun onClick(v: View?) {
        when (v?.parent) {
            recycler -> {
                val idx: Int = recycler.getChildAdapterPosition(v!!)
                val messageIntent = Intent(this.context, MessageActivity::class.java)
                messageIntent.putExtra("position", idx)
                startActivity(messageIntent)
                Log.v("position, idx = ", idx.toString())
                Log.v("Loading.. ", "MailActivity!")
            }

            user_recycler -> {
                val idx = user_recycler.getChildAdapterPosition(v!!)
                val data = AccountData.accountList.get(idx)
                selectedData = data
                readId = selectedData!!.id
                readPass = selectedData!!.pass

                var str = readId.split("@")
                email_main_text.text = str[0]
                email_sub_text.text = "@" + str[1]
                animateMenu()
            }

            nav_recycler -> {
                val idx = nav_recycler.getChildAdapterPosition(v!!)
                when (idx) {
                    //받은 메일
                    0 -> {
                        if (readTask.status == AsyncTask.Status.RUNNING) {
                            readTask.cancel(true)
                        }
                        readTask = ReadTask()
                        readTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                        drawer_layout.closeDrawer(GravityCompat.START)
                    }

                    //보낸메일
                    1 -> {
                    }
                }
            }
        }


        //finish()안하면 activity쌓일거임 근데 그러면 activity돌아갈때마다 프로그레스바 돌면서 메일 가져올텐데
        //상태를 저장해놓는 부분을 생각해야 할 듯


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

        if (accountList.isEmpty()) {
            //이메일 계정 등록 안되어있으면 sub_text GONE
            email_sub_text.visibility = View.GONE
        } else {
            //계정 등록 되있으면 VISIBLE
            email_sub_text.visibility = View.VISIBLE

            //계정리스트가 null이 아니지만 선택된 data가 없을 때, 리스트 마지막 값으로 read
            if (selectedData == null) {
                selectedData = accountList.last()
                readId = selectedData!!.id
                readPass = selectedData!!.pass
                email_main_text.text = readId.split("@")[0]
                email_sub_text.text = "@"+readId.split("@")[1]
                readTask = ReadTask()
                readTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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
            //backPressCloseHandler.onBackPressed()
            //  fm.popBackStack()
            System.out.println("작동")
            Toast.makeText(applicationContext, "메일 뒤로가기", Toast.LENGTH_SHORT)
            //super.onBackPressed();

            finish()
        }
    }


    inner class ReadTask : AsyncTask<Void, Void, Void>() {

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            read_progress.visibility = View.INVISIBLE
            try {
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
            val host = "imap." + readId.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            Log.v("hosthost", host)
            Log.v("idid", readId)
            Log.v("pass", readPass)
            val port = "993"

            try {
                val props = Properties()
                //Properties props = System.getProperties();
                props.setProperty("mail.store.protocol", "imap")
                props["mail.imap.host"] = host
                props["mail.imap.port"] = port//port
                // SSL setting
                props.setProperty("mail.imap.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory")
                props.setProperty("mail.imap.socketFactory.fallback", "false")
                props.setProperty("mail.imap.socketFactory.port",
                        port)

                val auth = object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(readId, readPass)
                    }
                }

                val session = Session.getDefaultInstance(props, auth)

                val store = session.getStore("imap")
                store!!.connect(host, readId, readPass)
                //IMAPFolder
                val folder = store.getFolder("INBOX") as IMAPFolder // This doesn't work for other email account
                folder.open(Folder.READ_WRITE)
                //folder = (IMAPFolder) store.getFolder("inbox"); This works for both email account


                //if(!folder.isOpen()) folder.open(Folder.READ_WRITE);
                val count = folder.messageCount

                val messages = folder.messages


                val states = 20 // 새로고침 할 때 + 30개 해주기
                for (i in messages.size - 1 downTo messages.size - states + 1) {

                    if (isCancelled) {
                        Log.v("hihihihi888", "cancel")
                        break
                    }
                    val msg = messages[i]
                    //System.out.println(msg.getMessageNumber());
                    //System.out.println(folder.getUID(msg)
                    val subject = MimeUtility.decodeText(msg.subject)
                    val from = MimeUtility.decodeText(msg.from[0].toString())
                    val date = msg.sentDate
                    val contenttype = msg.contentType
                    val size = msg.size
                    val content: Object = msg.content as Object

                    readList.add(ReadData(from, subject, date, contenttype, content, false))
                }
                if (folder != null && folder.isOpen) {
                    folder.close(true)
                }
                store?.close()
            } catch (e: NoSuchProviderException) {
                e.printStackTrace()
            } catch (e: MessagingException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.message
            }
            return null
        }


        fun receiveAttach(uri: String) {
            var file: File = File(uri)
            var totalFileSize: Long = 0

            var fileUri = uri
            var fileSize = file.length()
            var fileName = file.name
            var fileType = fileName.split('.')[1]

            for (attachData in rattach_list) {
                totalFileSize += attachData.receive_fileSize //기존 파일 크기
            }
            totalFileSize += fileSize //현재 select해서 가져온 파일 크기

            //gmail 20MB 이상일 시, naver 10MB 이상일 시(상황에 따라 처리 But 속도가 느릴 수 있음.)
            if (totalFileSize > 10485760) {
                System.out.println("파일첨부 10MB 넘어감")
                //Toast.makeText(this, "파일첨부는 10MB를 넘을 수 없습니다", Toast.LENGTH_SHORT).show()
            } else {
                rattach_list.add(RattachData(fileUri, fileType, fileName, fileSize))
                Log.v("fileInfo", rattach_list.toString())
                Log.v("totalFileSize", totalFileSize.toString())
            }
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
        Log.v("onResume", "resume")
        Log.v("accountList", AccountData.accountList.toString())


        readSetting() //read recycler에 대한 설정

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




