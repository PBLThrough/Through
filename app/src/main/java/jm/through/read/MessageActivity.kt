package jm.through.read

import android.os.Bundle
import jm.through.R
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import java.io.IOException
import jm.through.activity.MailActivity
import jm.through.read.FolderFetchImap.readList
import java.text.SimpleDateFormat
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.internet.MimeMultipart
import javax.mail.BodyPart
import javax.mail.internet.ContentType


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
                    if (bodyPart.isMimeType("text/plain")) {
                        result = bodyPart.content as String
//                } else if (bodyPart.isMimeType("text/html")) {
//                    val html = bodyPart.content as String
//                    result = org.jsoup.Jsoup.parse(html).text()
                    } else if (bodyPart.content is MimeMultipart) {
                        result = getTextFromMimeMultipart(bodyPart.content as MimeMultipart)
                    }
                    return result
                }

                if (multipartAlt)
                // alternatives appear in an order of increasing
                // faithfulness to the original content. Customize as req'd.
                    return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1))
                var result = ""
                for (i in 0 until count) {
                    val bodyPart = mimeMultipart.getBodyPart(i)
                    result += getTextFromBodyPart(bodyPart)
                }
                return result
            }

            @Throws(MessagingException::class, IOException::class)
            fun getTestFromMessage(message:Object) : String{
                var result = ""
                if (getContenttype.contains("text")) {
                    result = message.toString()
                    Log.v("multipart debug","text decoding")
                } else if (getContenttype.contains("multipart/*")) {
                    val mimeMultipart = message as MimeMultipart
                    result = getTextFromMimeMultipart(mimeMultipart)
                    Log.v("multipart debug","multipart decoding");
                }
                return result
            }

            var temp = getTestFromMessage(getContents)
            mWebView.loadData(temp,getContenttype,"UTF-8")

//            @Throws(MessagingException::class)
//            fun getMessageContent(message:Object): String {
//                try {
//                    val content = readList[a].mailContent
//                    if (content is Multipart) {
//                        val messageContent = StringBuffer()
//                        val multipart = content as Multipart
//                        for (i in 0 until multipart.count) {
//                            var part = multipart.getBodyPart(i)
//                            if (part.isMimeType("text/*")) {
//                                System.out.println("multipart's "+i+" decoding..");
//                                messageContent.append(part.content.toString())
//                            }
//                        }
//                        return messageContent.toString()
//                    }
//                    return content.toString()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//                return ""
//            }

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}