package jm.through.read

import android.os.Bundle
import android.os.PersistableBundle
import jm.through.R
import jm.through.read.FolderFetchImap.readList
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import java.text.SimpleDateFormat

/*
메일 상세보기 창
 */

class MessageActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);

        val t = findViewById<Toolbar>(R.id.message_bar)
        setSupportActionBar(t)
        supportActionBar!!.setTitle("MessageActivity 툴바!!")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val titleString = findViewById(R.id.message_content_title) as TextView // 제목
        val memoString = findViewById(R.id.message_content_memo) as TextView // 내용(Only text)
        val dateString = findViewById(R.id.message_content_time) as TextView // 시각
        val mWebView = findViewById(R.id.message_content_web) as WebView // 내용 content

        //final TextView itemString = (TextView)v.findViewById(R.id.)
        val df = SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm")

        if (getIntent() != null) {
            val intent = getIntent();
            val a = intent.getIntExtra("position",0);

            Log.v("intent "," is "+a);
            Log.v("Loading.. ","MessageActivity!");
            val getSubject = readList[a].mailTitle.split("<".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0] // subject
            val getFrom = readList[a].mailMemo // from
            val getDate = readList[a].mailDate // date
            val getWeb = readList[a].mailMemo
            // ImageView getimage= readList.get(a).component4();
            //System.out.println("subject printing... "+ getSubject.toString());

            val mTime = df.format(getDate)

            dateString.text = mTime
            titleString.text = getSubject
            memoString.text = getFrom
//           = getWeb
            //   imageView.setImageURI();
        }

    }
}