package jm.through.send

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import jm.through.R
import kotlinx.android.synthetic.main.fragment_send.*
import android.R.attr.data
import android.annotation.TargetApi
import android.provider.MediaStore.Images.Media.getBitmap
import android.graphics.Bitmap
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.os.Build
import android.provider.MediaStore
import android.content.CursorLoader
import android.R.attr.data
import android.annotation.SuppressLint
import android.net.Uri
import android.provider.DocumentsContract




class SendFragment : Fragment() {
     val REQ_PICK_CODE = 100
     var attach_url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view: View = inflater!!.inflate(R.layout.fragment_send, container, false)
        var send_btn = view.findViewById(R.id.send) as Button //메일 보내기 버튼
        var attach_btn= view.findViewById(R.id.attach) as Button //첨부파일 보내기 버튼


        attach_btn.setOnClickListener{

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, REQ_PICK_CODE)


        }




        send_btn.setOnClickListener {

            //Exception 나서 네트워크 연결 시 Thread사용
            Thread {
                run {
                    try {
                        var recipient = edit_receiver.text.toString().trim()
                        var subject = edit_title.text.toString().trim()
                        var body = email_body.text.toString().trim()


                        //서버에서 받아와야하는 정보 id, pwd(임의로 넣음)
                        var sender: MailSender = MailSender("",
                                "")

                        //받는사람, 제목, 내용은 변수로 받고 보내는 이는 서버의 user정보
                        //Mail을 보내는 부분
                        sender.sendMail(subject,
                                "youremail", recipient, body, attach_url)
                    } catch (e: Exception) {
                        Log.e("SendMail", e.message)
                    }
                }
            }.start()
        }

        return view

    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if(requestCode == REQ_PICK_CODE)
            {
                val uri = data!!.data
                attach_url = getRealPathFromURI(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun getRealPathFromURI(uri: Uri): String {
        var filePath = ""
        val fileId = DocumentsContract.getDocumentId(uri)
        // Split at colon, use second item in the array
        val id = fileId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val selector = MediaStore.Images.Media._ID + "=?"
        val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, arrayOf(id), null)
        val columnIndex = cursor.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()

        Log.v("hihi", filePath)
        return filePath

    }
}
