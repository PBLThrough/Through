package jm.through.read

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import jm.through.R
import jm.through.R.id.toolbar
import jm.through.attachment.RattachAdapter
import jm.through.attachment.RattachData
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.rattach_item.*
import java.io.File
import java.net.URISyntaxException

class ReadActivity : AppCompatActivity() {
    lateinit var  receiveAdapter: RattachAdapter
    var context: Context =this
    var rattach_url : String? =null
    var rattach_list:ArrayList<RattachData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_content)

        receiveAdapter = RattachAdapter(this,rattach_list)
        content_recycler.adapter = receiveAdapter
        content_recycler.layoutManager = LinearLayoutManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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