package jm.through.read

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import jm.through.R
import jm.through.read.FolderFetchImap.readList
import java.io.IOException
import java.text.SimpleDateFormat
import javax.mail.BodyPart
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.internet.ContentType
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeUtility


/*
메일 상세보기 창
 */


class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Log.v("Loading.. ", "MessageActivity!");

        /*
        boundary = 본문의 서로 다른 부분을 구분하기 위한 구분자로 쓰임
        본문에서 boundary 뽑고 따로 읽기

        */
//        // 여기서 에러남
//        val t = findViewById<Toolbar>(R.id.message_bar)
//        setSupportActionBar(t)
//        supportActionBar!!.setTitle("MessageActivity 툴바!!")
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Log.v("messageActivity - ", "toolbar ");
        val titleString = findViewById(R.id.message_content_title) as TextView // 제목
        val memoString = findViewById(R.id.message_content_memo) as TextView // 내용(Only text)
        val dateString = findViewById(R.id.message_content_time) as TextView // 시각
        val mWebView = findViewById(R.id.message_content_web) as WebView // 내용 content

        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.defaultTextEncodingName = "UTF-8"

        val df = SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm")

        Log.v("messageActivity -", "getIntent()");
        if (getIntent() != null) {
            val intent = getIntent();
            val a = intent.getIntExtra("position", 0);
            Log.v("intent ", " is " + a);
            val getSubject = readList[a].mailTitle.split("<".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] // subject
            val getFrom = readList[a].mailMemo // from
            val getDate = readList[a].mailDate // date
            val getContents = readList[a].mailContent
            val getContenttype = readList[a].mailContenttype


            //val mp = readList[a].mailContent as Multipart // test
            val mTime = df.format(getDate)

            dateString.text = mTime
            titleString.text = getSubject
            memoString.text = getFrom

            System.out.println("mail's contenttype : " + getContenttype);


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
//                        else if (bodyPart.isMimeType("text/html")) {
//                            result = bodyPart.content as String}
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

                    //TODO Comment by jiran
                    /** contains는 문구가 포함됬는지를 확인하는 api임.
                    contents-type이 multipart/alternative일 때 아래 분기문이 스킵되면서 컨텐츠를 획득하지 않음.

                    + 질문 있습니다!! multipart로 구문을 찾는데 multipart/alternative 인 경우 분기문이 어떤 이유로
                    스킵되는 건가요..?
                     */
                } else if (getContenttype.contains("multipart", true)) {
                    val mimeMultipart = message as MimeMultipart
                    result = getTextFromMimeMultipart(mimeMultipart)
                    Log.v("multipart debug","multipart decoding");
                }

                return MimeUtility.decodeText(result)
            }

            /**
             * HTML URL 할 것
             * 순서 1. 주소 클릭 시 바로 이동하지 않고 이동할 주소 띄우기
             *      방법 1. a href 앞에 새로운 a href를 넣어 이동할 주소를 묻는 걸 띄워보기
             * 순서 2. Yes 시 이동 O, No 시 이동 X
             * 순서 3. 주소 클릭 시 바로 이동하지 않고 이동할 주소 오픈 그래프로 띄우기
             *
             * 아래 = 웹페이지 용 마우스를 링크에 가져다대면 상태표시줄에 링크 표
             * <a href="http://aaa.com" onclick="window.open(this.href, '', ''); return false;">
             *
             * 테스트 해볼 것
             * 아래 = href에서 발생한 액션을 실행시키지 않는 = 슈도프로토
             * <a href="#" onclick="return false;">test</a>
             *
             * <a onclick="myFunc();">Hello</a>
             *
             * $(document).ready(function(){ $('a').css('cursor','pointer');});
             *
             * href 를 찾아 onClick속성을 추가시켜 커스텀 함수로(링크를 보여주는 곳) 이동시켜야함
             * onclick="callFunction(); return false;
             */

            fun JavaScriptParser(mContext: Context) {
                @JavascriptInterface
                fun showToast(toast: String) {
                    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
                }
            }

          //  mWebView.addJavascriptInterface(JavaScriptPasser(this), "Android")

            //TODO Comment by jiran
            /* 메일 파싱 부분은 Network이기 때문에 별도의 WorkingThread로 변경
            (별도 스레드로 추출하지 않으면, NetworkOnMainThreadException 발생.) */
            Thread(Runnable {
                var temp = getTestFromMessage(getContents)

//                val count = StringUtils.countOccurrences(temp, "<a href")
                //TODO Comment by jiran
                /* UI 컴포넌트의 API는 메인스레드(UI)에서만 요청할 수 있음
                (GUI 프로그래밍은 동기화 문제 방지를 위해 모두 같은 개념을 가짐.) */
                runOnUiThread({ mWebView.loadData(temp, "text/html", "UTF-8"); })
            }).start()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}

