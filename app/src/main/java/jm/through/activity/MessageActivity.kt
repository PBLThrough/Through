package jm.through.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import jm.through.R
import jm.through.fragment.urlDialogFragment
import jm.through.function.FolderFetchImap.readList
import java.io.IOException
import java.text.SimpleDateFormat
import javax.mail.BodyPart
import javax.mail.MessagingException
import javax.mail.internet.ContentType
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeUtility
import jm.through.function.mWebViewClient

/*
메일 상세보기 창
 */

class MessageActivity : AppCompatActivity() {
    lateinit var mWebViewClient:WebViewClient
    lateinit var urlDialogFragment: urlDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        /** 툴바 적용 */
        val t = findViewById(R.id.message_bar) as Toolbar
        setSupportActionBar(t)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /** message xml 변수 */
        val titleString = findViewById(R.id.message_content_title) as TextView // 제목
        val dateString = findViewById(R.id.message_content_time) as TextView // 시각
        val mWebView = findViewById(R.id.message_content_web) as WebView // 내용 content

        /** 웹뷰 적용 */
        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.setBuiltInZoomControls(false) // 확대 축소 기능
        webSettings.setLoadsImagesAutomatically(true)

        mWebViewClient = jm.through.function.mWebViewClient(this)
        mWebView.webViewClient = mWebViewClient

        val df = SimpleDateFormat("yyyy.MM.dd EE, aa hh:mm")

        /** 메일 요소 변수로 가져오기 */
        Log.v("messageActivity -", "getIntent()");
        if (getIntent() != null) {
            val intent = getIntent();
            val a =intent.extras["position"] as Int
            Log.v("intent ", " is " + a)

            //TODO 여기 한번씩 null exception 나니까 null 처리 해줘
            var getSubject = readList[a].mailTitle  // subject
            var getDate = readList[a].mailDate // date
            var getContents = readList[a].mailContent
            var getContenttype = readList[a].mailContenttype
            var mTime = df.format(getDate)

            if(getSubject ==null) getSubject = "제목 없음"

            dateString.text = mTime
            titleString.text = getSubject

            /** 메일 디코딩 */
            @Throws(MessagingException::class, IOException::class)
            fun getTextFromMimeMultipart(mimeMultipart:MimeMultipart):String{
                val count = mimeMultipart.count
                if (count == 0)
                    throw MessagingException("Multipart with no body parts not supported.")
                val multipartAlt = ContentType(mimeMultipart.contentType).match("multipart/alternative")

                fun getTextFromBodyPart(bodyPart: BodyPart):String{
                    var result = ""

                    try {
                        if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                            result = bodyPart.content as String
                        }
                        else if (bodyPart.content is MimeMultipart){
                            result = getTextFromMimeMultipart(bodyPart.content as MimeMultipart)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    return MimeUtility.decodeText(result)
                }

                // Multipart ERROR
                if (multipartAlt)
                    return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1))
                var result = ""
                for (i in 0 until count) {
                    val bodyPart = mimeMultipart.getBodyPart(i)
                    // 실행 안됨
                    Log.v("running"+i.toString()," 번째 도는 중..")
                    result += getTextFromBodyPart(bodyPart)
                }
                return MimeUtility.decodeText(result)
            }

            @Throws(MessagingException::class, IOException::class)
            fun getTestFromMessage(message:Object) : String{
                var result = ""
                if (getContenttype.contains("text")) {
                    result = message.toString()
                    Log.v("multipart debug","text decoding")
                } else if (getContenttype.contains("multipart", true)) {
                    val mimeMultipart = message as MimeMultipart
                    result = getTextFromMimeMultipart(mimeMultipart)
                    Log.v("multipart debug","multipart decoding");
                }
                return MimeUtility.decodeText(result)
            }


//            fun replaceFragment(){
//                val urlFragment =
//                urlFragment.show(supportFragmentManager, "url 다이얼로그")
//            }

            fun JavaScriptParser(mContext: Context) {
                @JavascriptInterface
                fun showToast(toast: String) {
                    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
                }
            }

            //TODO Comment by jiran
            /** 메일 파싱 부분은 Network이기 때문에 별도의 WorkingThread로 변경
            (별도 스레드로 추출하지 않으면, NetworkOnMainThreadException 발생.) */
            Thread(Runnable {
                var temp = getTestFromMessage(getContents)
                //TODO Comment by jiran
                /** UI 컴포넌트의 API는 메인스레드(UI)에서만 요청할 수 있음
                (GUI 프로그래밍은 동기화 문제 방지를 위해 모두 같은 개념을 가짐.) */
                runOnUiThread({ mWebView.loadData(temp, "text/html", "UTF-8"); })
            }).start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.readbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                android.R.id.home -> { //toolbar의 back키 눌렀을 때 동작
                    finish()
                    return true
                }

        /** 이곳에다 toolbar item 추가해야하는데 불러오지 못함
         * toolbar item = res/menu/readbar */

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    fun showDialog(url:String){
        var urlDialogFragment = urlDialogFragment()
        Log.v("showDialog","Dialog show")
        if(url.contains("http")){
            urlDialogFragment.show(supportFragmentManager,"미리보기")
        }
    }

}


