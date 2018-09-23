package jm.through.read

import android.os.Bundle
import android.os.PersistableBundle
import jm.through.R
import jm.through.read.FolderFetchImap.readList
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import javax.mail.BodyPart
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.Part
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart


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

        // 세로 scroll 제거
        //mWebView .setHorizontalScrollBarEnabled(false);
// 가로 scroll 제거
        //mWebView .setVerticalScrollBarEnabled(false);

        // mWebView.loadUrl("https://...");
        // mWebView.loadDataWithBaseURL(baseUrl, data, mimetype, encoding, historyUrl);
        //mWebView.loadData()


        //final TextView itemString = (TextView)v.findViewById(R.id.)
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
            val getContent = readList[a].mailContent
            //val getWeb = readList[a].mailMemo
            val getContenttype = readList[a].mailContenttype
            // ImageView getimage= readList.get(a).component4();
            //System.out.println("subject printing... "+ getSubject.toString());
            val mp = readList[a].mailContent as Multipart // test
            System.out.println("mail's contenttype : "+getContenttype);
            val mTime = df.format(getDate)

            var messageContent = ""
            var attachFiles = ""
            var saveDirectory = "" // dir

            dateString.text = mTime
            titleString.text = getSubject
            memoString.text = getFrom


            if (getContenttype.contains("multipart")) {
                // content may contain attachments
                val multiPart = getContent as Multipart
                val numberOfParts = multiPart.count

                for (partCount in 0 until numberOfParts) {
                    val part = multiPart.getBodyPart(partCount) as MimeBodyPart
                    println("mimePart num - $partCount !!")
                    if (Part.ATTACHMENT.equals(part.disposition, ignoreCase = true)) {
                        // this part is attachment
                        val fileName = part.fileName
                        attachFiles += "$fileName, "
                        //part.saveFile(saveDirectory + File.separator + fileName);
                    } else {
                        // this part may be the message content
                        messageContent = part.content.toString()
                    }
                }

                if (attachFiles.length > 1) {
                    attachFiles = attachFiles.substring(0, attachFiles.length - 2)
                }
            }
            else if (getContenttype.contains("text/plain") || getContenttype.contains("text/html")) {
                val content = getContent
                if (content != null) {
                    println("we have content!!")
                    messageContent = content!!.toString()
                }
            }




            //if (getContenttype == "text/html"){
            //mWebView.loadData(getContent.toString(),getContenttype,"UTF-8");
            mWebView.loadData(messageContent,getContenttype,"UTF-8");

//
//            if (getContenttype.contains("multipart")) {
//                val multiPart = getContent as Multipart
//                for (int i = 0; i < multiPart.getCount(); i++)
//                    val part = multiPart.getBodyPart(i) as MimeBodyPart
//                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
//                    // this part is attachment
//                    // code to save attachment...
//                }
//                // save an attachment from a MimeBodyPart to a file
//                val destFilePath = "D:/Attachment/" + part.fileName
//                val output = FileOutputStream(destFilePath)
//                val input = part.inputStream
//                val buffer = ByteArray(4096)
//                var byteRead: Int
//                while ((byteRead = input.read(buffer)) != -1) {
//                    output.write(buffer, 0, byteRead)
//                }
//                output.close()

//            if (getContenttype == "text/plain" || getContenttype =="text/html"){
//                mWebView.loadData(getContent.toString(),getContenttype,"UTF-8");
//            }
//
//            if(getContenttype =="multipart/alternative"){
//                val boundaryArray:Array<String>;
//                val getBoundary= getContenttype.split("=")[1];
//                val getBoundaryCount = getContent.toString().indexOf(getBoundary);
//                // boundary 있는 곳까지 읽고 그 다음으로 넘어간 후 또 읽기
//                for(i in 0..getBoundaryCount) {
//                    var getlistboundary = getContent.toString().substring(0);
//                    //mWebView.loadData(getContent.toString(), getContenttype, "UTF-8");
//                }
//            }

            //mWebView.loadData(getFrom,getContenttype,"UTF-8");
            //}
            //else if(getContenttype == "multipart/alternative"){
               // mWebView.lo



            //   imageView.setImageURI();
        }

    }
}