package jm.through.read

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import jm.through.R
import jm.through.activity.MailActivity
import jm.through.attachment.RattachAdapter
import jm.through.attachment.RattachData
import kotlinx.android.synthetic.main.fragment_content.*
import java.io.File

class ReadActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var  receiveAdapter: RattachAdapter
    var context: Context =this
    var rattach_url : String? =null
    var rattach_list:ArrayList<RattachData> = ArrayList()

    lateinit var rAdapter: ReadAdapter //recycler연결시킬 adapte
    lateinit var checkRecycler: RecyclerView
    lateinit var readProgress: ProgressBar
    private var swipeContainer: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_content)
        Log.v("Loading.. ","ReadActivity!");
        var readTask = ReadTask()
        readTask.execute()

        receiveAdapter = RattachAdapter(this,rattach_list)
        content_recycler.adapter = receiveAdapter
        content_recycler.layoutManager = LinearLayoutManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    fun inflating(inflager: LayoutInflater?, container:ViewGroup?,saveInstanceState: Bundle? ):View{
        val checkView = layoutInflater.inflate(R.layout.fragment_check, container) as View
        //val checkView: View = layoutInflater().inflate(R.layout.fragment_check,container,false);

        checkRecycler = checkView.findViewById(R.id.recycler) as RecyclerView
        readProgress = checkView.findViewById(R.id.read_progress) as ProgressBar
        return checkView
    }

    // 셀 하나를 클릭했을 때 위치를 넘겨주는 곳
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View) {
        val idx: Int = checkRecycler.getChildAdapterPosition(v)

        //MessageFragment 에 전달할 bundle

        //MessageActivity 에 전달할 intent
        var m_intent = Intent();
        m_intent.putExtra("position",idx);

        //var message = MessageFragment() as Fragment//메일 보내는 fragment
        var message = MessageActivity() as Activity;
        //var fm = fragmentManager //fragment교체에 필요한 fragmentManager

        // intent( 현재 액티비티, 전환할 액티비티)
        val intent= Intent(this, MessageActivity::class.java);
        startActivity(intent);
        //message.arguments = bundle;
        //fm.beginTransaction().replace(R.id.fragment_container, message).commit()
    }

    fun addRattach(url:String){
        var downFile: File = File(url)
        var totalSize:Long = 0

        var downFileUrl = url
        var downFileSize = downFile.length()
        var downFileName = downFile.name
        var downFileType = downFileName.split('.')[1]

        for (rattachData in rattach_list){
            totalSize += rattachData.receive_fileSize
        }
        totalSize += downFileSize
        // 크기제한 알아보기
    }

    inner class ReadTask : AsyncTask<Void, Void, Void>() {
        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

//        override fun onPostExecute(result: Void?) {
//            super.onPostExecute(result)
//            readProgress.visibility = View.INVISIBLE
//            try {
//                rAdapter = ReadAdapter(FolderFetchImap.readList)
//                rAdapter.setOnItemClickListener(context as MailActivity)
//                checkRecycler.adapter = rAdapter
//                checkRecycler.layoutManager = LinearLayoutManager(this@ReadActivity)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Log.v("fail", "")
//            }
//        }

        override fun doInBackground(vararg params: Void?): Void? {
            var reader = FolderFetchImap()
            //var reader = MailReader()

            // 업데이트 할 땐 아이디와 비밀번호 바꾸기 ^^!!
            reader.readImapMail("yourmail","yourpass")
            Log.v("list", FolderFetchImap.readList.toString())

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

//
//    fun downPath(context:Context,url: Uri):String? {
//        var url = url
//        var needToCheckUrl = Build.VERSION.SDK_INT >= 19
//        var itemSelection: String? = null
//        var itemSelectionArgs : Array<String>? =null
//
//        // 1. 받은 파일 contenttype 체크
//        // 2. 저장공간 권한 얻기 (다운z로드함에 저장하기 위해)
//        // 3. 파일 저장
//
//    }




}