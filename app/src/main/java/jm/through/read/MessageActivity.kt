package jm.through.read

import android.os.Bundle
import android.os.PersistableBundle
import jm.through.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import jm.through.activity.MailActivity
import jm.through.activity.MailActivity.Task.readList
import java.text.SimpleDateFormat

/*
메일 상세보기 창
 */

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Log.v("Loading.. ", "MessageActivity!");

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

        // 세로 scroll 제거
        //mWebView .setHorizontalScrollBarEnabled(false);
// 가로 scroll 제거
        //mWebView .setVerticalScrollBarEnabled(false);

        // mWebView.loadUrl("https://...");
        // mWebView.loadDataWithBaseURL(baseUrl, data, mimetype, encoding, historyUrl);
        //mWebView.loadData()


        //final TextView itemString = (TextView)v.findViewById(R.id.)
        val df = SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm")

        Log.v("messageActivity -", "getIntent()");
        if (getIntent() != null) {
            val intent = getIntent();
            val a = intent.getIntExtra("position", 0);
            //readList.add(new ReadData(from, subject, date, contenttype,/* content,*/ false);
            Log.v("intent ", " is " + a);
            val getSubject = readList[a].mailTitle.split("<".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] // subject
            val getFrom = readList[a].mailMemo // from
            val getDate = readList[a].mailDate // date
            val getContent = readList[a].mailContent
            //val getWeb = readList[a].mailMemo
            val getContenttype = readList[a].mailContenttype
            // ImageView getimage= readList.get(a).component4();
            //System.out.println("subject printing... "+ getSubject.toString());

            System.out.println("mail's contenttype : " + getContenttype);
//            val mTime = df.format(getDate)
//
//            dateString.text = mTime
            //여기 터짐(난 왠지 모르겟..)
            titleString.text = getSubject
            memoString.text = getFrom

            //if (getContenttype == "text/html"){
            mWebView.loadData(getContent.toString(), getContenttype, "UTF-8");
            //mWebView.loadData(getFrom,getContenttype,"UTF-8");
            //}
            //else if(getContenttype == "multipart/alternative"){
            // mWebView.lo


            //   imageView.setImageURI();
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}