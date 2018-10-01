package jm.through.read

import android.os.Bundle
import jm.through.R
import jm.through.read.FolderFetchImap.readList
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebView
import android.widget.TextView
import java.io.IOException
import java.text.SimpleDateFormat
import javax.mail.MessagingException
import javax.mail.Multipart


/*
메일 상세보기 창
 */

class MessageActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Log.v("Loading.. ","MessageActivity!");


        /*
        boundary = 본문의 서로 다른 부분을 구분하기 위한 구분자로 쓰임
        본문에서 boundary 뽑고 따로 읽기

        */
//        // 여기서 에러남
//        val t = findViewById<Toolbar>(R.id.message_bar)
//        setSupportActionBar(t)
//        supportActionBar!!.setTitle("MessageActivity 툴바!!")
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        Log.v("messageActivity - ","toolbar ");
        val titleString = findViewById(R.id.message_content_title) as TextView // 제목
        val memoString = findViewById(R.id.message_content_memo) as TextView // 내용(Only text)
        val dateString = findViewById(R.id.message_content_time) as TextView // 시각
        val mWebView = findViewById(R.id.message_content_web) as WebView // 내용 content

        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)

        val df = SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm")

        Log.v("messageActivity -","getIntent()");
        if (getIntent() != null) {
            val intent = getIntent();
            val a = intent.getIntExtra("position",0);

            //readList.add(new ReadData(from, subject, date, contenttype,/* content,*/ false);
            Log.v("intent "," is "+a);
            val getSubject = readList[a].mailTitle.split("<".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] // subject
            val getFrom = readList[a].mailMemo // from
            val getDate = readList[a].mailDate // date
            val getContents = readList[a].mailContent
            val getContenttype = readList[a].mailContenttype

            val mp = readList[a].mailContent as Multipart // test
            System.out.println("mail's contenttype : "+mp);
            val mTime = df.format(getDate)

            dateString.text = mTime
            titleString.text = getSubject
            memoString.text = getFrom


            mWebView.loadData(getContents.toString(),getContenttype,"UTF-8");

            @Throws(MessagingException::class)
            fun getMessageContent(message:Object): String {
                try {
                    val content = readList[a].mailContent
                    if (content is Multipart) {
                        val messageContent = StringBuffer()
                        val multipart = content as Multipart
                        for (i in 0 until multipart.count) {
                            var part = multipart.getBodyPart(i)
                            if (part.isMimeType("text/*")) {
                                System.out.println("multipart's "+i+" decoding..");
                                messageContent.append(part.content.toString())
                            }
                        }
                        return messageContent.toString()
                    }
                    return content.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return ""
            }


            if (getContenttype.contains("multipart")) {
                var temp = getMessageContent(getContents)

                System.out.println("decoding's over! ");
                mWebView.loadData(temp,getContenttype,"UTF-8")

            }

        }

    }

}