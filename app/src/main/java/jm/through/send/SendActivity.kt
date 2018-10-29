package jm.through.send

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
import android.widget.Toast
import com.pchmn.materialchips.adapter.ChipsAdapter.mChipList
import jm.through.attachment.AttachAdapter
import jm.through.attachment.AttachData
import jm.through.form.FormActivity
import kotlinx.android.synthetic.main.app_bar_mail.*
import kotlinx.android.synthetic.main.recipient_item.*
import java.io.File
import java.net.URISyntaxException


class SendActivity : AppCompatActivity() {
    lateinit var rAdapter: AttachAdapter //recycler연결시킬 adapter
    var context: Context = this
    val REQ_PICK_CODE = 100
    var attach_uri: String? = null
    var attach_list: ArrayList<AttachData> = ArrayList()

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
        toolbar.title = "메일 쓰기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
                } else {
                    goMail()
                }

                return true
            }
        }

        return false
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
                    Log.v("start sending", attach_list.toString())

                    var recipientList = ArrayList<String>()

                    for (chip in mChipList) {
                        recipientList.add(chip.label)
                    }

                    var subject = edit_title.text.toString().trim()
                    var body = email_body.text.toString().trim()

                    var sender: MailSender = MailSender("dream7739@naver.com",
                            "jmzzang7739")
                    var flag = sender.sendMail(subject,
                            "dream7739@naver.com", recipientList, body, attach_list)

                    Log.v("flagflag", flag.toString())
                    if (flag) {
                        this.runOnUiThread({ Toast.makeText(this, "메일 전송 성공", Toast.LENGTH_SHORT).show() })
                    } else {
                        this.runOnUiThread({ Toast.makeText(this, "메일 발송 실패", Toast.LENGTH_SHORT).show() })
                    }
                } catch (e: Exception) {
                    Log.e("SendMailLog", e.message)
                }
            }
        }.start()


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


