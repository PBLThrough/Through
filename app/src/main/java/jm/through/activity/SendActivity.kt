package jm.through.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import jm.through.R
import kotlinx.android.synthetic.main.fragment_send.*
import android.annotation.TargetApi
import android.os.Build
import android.provider.MediaStore
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.pchmn.materialchips.adapter.ChipsAdapter.mChipList
import jm.through.AccountData.accountList
import jm.through.UserData
import jm.through.adapter.AttachAdapter
import jm.through.data.*
import jm.through.data.SignInResult.EmailData
import jm.through.fragment.AddDialogFragment
import jm.through.function.MailSender
import jm.through.fragment.SendBarFragment
import jm.through.fragment.TrustDialogFragment
import jm.through.network.ApplicationController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URISyntaxException


class SendActivity : AppCompatActivity() {
    lateinit var rAdapter: AttachAdapter //recycler연결시킬 adapter
    lateinit var barTitle: TextView
    lateinit var spinBtn: ImageButton
    lateinit var accountinfo: DetailData
    var context: Context = this
    val REQ_PICK_CODE = 100
    var attach_uri: String? = null
    var click = true
    var attach_list: ArrayList<AttachData> = ArrayList()
    val sendbarFragment = SendBarFragment()
    lateinit var sendBar: Toolbar


    @RequiresApi(Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_send)
        addToolbar()
        addRecycler()

        //폼 버튼 눌렀을 시
        form_btn.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.from, R.anim.stay) //애니메이션
        }

        //첨부 파일 버튼 눌렀을 시
        attach_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, REQ_PICK_CODE)

        }

        //주소록 버튼 눌렀을 시
        adressBtn.setOnClickListener {
            val intent = Intent(this, TrustActivity::class.java)
            startActivity(intent)
        }


        //인텐트 있으면
        if (intent != null) {
            var detail = intent.getStringExtra("formDetail")
            email_body.setText(detail)
            email_body.requestFocus() //포커스 주기
        }

    }


    fun addRecycler() {
        rAdapter = AttachAdapter(this, attach_list)
        attach_recycler.adapter = rAdapter
        attach_recycler.layoutManager = LinearLayoutManager(this)
    }


    fun addToolbar() {
        sendBar = findViewById(R.id.sendbar) as Toolbar
        spinBtn = sendBar.findViewById(R.id.send_spin_btn) as ImageButton
        barTitle = sendBar.findViewById(R.id.toolbar_title) as TextView

        setSupportActionBar(sendBar)
        defaultSender()
        spinButton()

        supportActionBar!!.setDisplayShowTitleEnabled(false) //기본 타이틀 보여줄지 말지 설정
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun spinButton() {

        spinBtn.setOnClickListener {
            ObjectAnimator.ofFloat(spinBtn, "rotation", if (click) 180f else 0f).start()


            if (click) {
                val fm = supportFragmentManager
                val tr = fm.beginTransaction()
                tr.add(R.id.send_frame, sendbarFragment).commit()
                send_frame.bringToFront() //최상위 뷰로 올림
                Log.v("open", "open")
            } else {
                val fm = supportFragmentManager
                val tr = fm.beginTransaction()
                tr.remove(sendbarFragment).commit()
            }
            click = !click

        }
    }


    //초기 툴바 설정 시 선택한 것이 없으면 첫번째 값으로 세팅
    fun defaultSender() {
        var barText = accountList.get(0).id
        var splitText = barText.split("@")
        barTitle.text = splitText[0] + "\n" + "@" + splitText[1]

        accountinfo = accountList.get(0)
    }

    //아이템 클릭시 보내는 사람 지정(클릭했을 때만 동작)
    fun setSender(position: Int) {
        accountinfo = accountList.get(position)

        var barText = accountinfo.id
        var splitText = barText.split("@")
        barTitle.text = splitText[0] + "\n" + "@" + splitText[1]
    }

    //클릭 후 메뉴 닫아주기
    fun closeRecycler() {
        ObjectAnimator.ofFloat(spinBtn, "rotation", if (click) 180f else 0f).start()
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        tr.remove(sendbarFragment).commit()
        click = !click
    }


    //toolbar menu create
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sendmenu, menu)
        return true
    }

    //toolbar item select
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            android.R.id.home -> finish() //뒤로 가기 누르면 꺼지게

            R.id.action_send -> {
                if (mChipList.isEmpty() ||
                        edit_title.text.toString().trim() == "") {
                    Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
                } else if (!checkRecipient()) {

                    var annonymousList = getRecipientList()

                    val dialog = TrustDialogFragment()

                    var bundle = Bundle()
                    bundle.putStringArrayList("annymous", annonymousList)

                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "신뢰 다이얼로그")
                } else {
                    goMail()
                }

                return true
            }
        }

        return false
    }

    fun getRecipientList(): ArrayList<String>? {

        var checked: Boolean
        var anonymousList = ArrayList<String>()

        for (recipient in mChipList) {
            var recipt = recipient.label

            checked = false

            for (i in UserData.trustList) {
                var str = i.email
                if (str == recipt) {
                    checked = true
                    break
                }
            }

            if (!checked) {
                anonymousList.add(recipt)
            }

        }


        return anonymousList
    }


    fun checkRecipient(): Boolean {

        var checked: Boolean


        for (recipient in mChipList) {
            var recipt = recipient.label

            checked = false

            for (i in UserData.trustList) {
                var str = i.email
                if (str == recipt) {
                    checked = true
                }
            }

            if (!checked) {
                return false
            }

        }


        return true
    }


    //file manager select result

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_PICK_CODE) {
                try {
                    val uri = data!!.data
                    attach_uri = getPath(this, uri)
                    addAttach(attach_uri!!) //첨부파일 첨부
                    Log.v("afterUri", attach_uri)
                } catch (e: Exception) {
                    Log.v("Fail", e.message)
                    Toast.makeText(this, "파일을 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                }
                rAdapter.notifyDataSetChanged() //addAttach될 시 list내용이 변경되기 때문에 adapter에 알리기
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun addAttach(uri: String) {
        var file: File = File(uri)
        var totalFileSize: Long = 0

        var fileUri = uri
        var fileSize = file.length()
        var fileName = file.name
        var fileType = fileName.split('.')[1]

        for (attachData in attach_list) {
            totalFileSize += attachData.fileSize //기존 파일 크기
        }
        totalFileSize += fileSize //현재 select해서 가져온 파일 크기

        //gmail 20MB 이상일 시, naver 10MB 이상일 시(상황에 따라 처리 But 속도가 느릴 수 있음.)
        if (totalFileSize > 10485760) {
            Toast.makeText(this, "파일첨부는 10MB를 넘을 수 없습니다", Toast.LENGTH_SHORT).show()
        } else {
            attach_list.add(AttachData(fileUri, fileType, fileName, fileSize))
            Log.v("fileInfo", attach_list.toString())
            Log.v("totalFileSize", totalFileSize.toString())
        }
    }


    //sending email
    @TargetApi(Build.VERSION_CODES.M)
    fun goMail() {

        Thread {
            run {
                try {
                    var recipientList = ArrayList<String>()

                    for (chip in mChipList) {
                        recipientList.add(chip.label)
                    }

                    var subject = edit_title.text.toString().trim()
                    var body = email_body.text.toString().trim()

                    var sender = MailSender(accountinfo.id,
                            accountinfo.pass)
                    var flag = sender.sendMail(subject,
                            accountinfo.id, recipientList, body, attach_list)

                    if (flag) {
                        this.runOnUiThread { Toast.makeText(this, "메일 전송 성공", Toast.LENGTH_SHORT).show() }
                        addTrustList(recipientList)
                    } else {
                        this.runOnUiThread { Toast.makeText(this, "메일 발송 실패", Toast.LENGTH_SHORT).show() }
                    }
                } catch (e: Exception) {
                    Log.e("SendMailLog", e.message)
                }
            }
        }.start()


    }

    fun addTrustList(recipientList: ArrayList<String>) {
        /**서버 통신 -> 신뢰 리스트 서버로 보내기*/

        val networkService = ApplicationController.instance!!.networkService
        val addTrustData = AddTrustData()
        addTrustData.token = UserData.token
        addTrustData.list = recipientList

        val addTrustCallback = networkService!!.addTrustList(addTrustData)

        addTrustCallback.enqueue(object : Callback<AddTrustResult> {
            override fun onResponse(call: Call<AddTrustResult>, response: Response<AddTrustResult>) {
                if (response.isSuccessful) {
                    Log.v("status",response.code().toString())

                    when (response.code()) {
                        200 -> {
                            for ( i in recipientList){
                                var data = SignInResult().EmailData()
                                data.email = i
                                UserData.trustList.add(data)
                            }

                        }
                        404 -> {
                            Log.v("status 404", "미확인 된 유저입니다")
                        }
                        409 -> {
                            Log.v("status 409", "중복된 유저입니다")
                        }

                    }

                }
            }

            override fun onFailure(call: Call<AddTrustResult>, t: Throwable) {
                Toast.makeText(applicationContext, "네트워크 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
            }

        })

    }


    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                Log.v("hi1", "hi")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                Log.v("hi2", "hi")
                var id = DocumentsContract.getDocumentId(uri);
                Log.v("downloadId", id.toString())

                //raw 파일 처리
                if (id.split(":")[0] == "raw") {
                    Log.v("rawdata", "hihi")
                    return id.split(":")[1]
                } else {
                    Log.v("norawdata", "hihi")
                    var contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), id.toLong())
                    return getDataColumn(context, contentUri, null, null)
                }
            } else if (isMediaDocument(uri)) {
                Log.v("hi3", "hi")
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
            }

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            Log.v("hi5", "hi")
            return uri.path
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


}


