package jm.through.read;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun.mail.iap.Argument;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

import jm.through.R;

import static jm.through.read.FolderFetchImap.readList;
//import static jm.through.read.MailReader.readList;

/**
 * 메일 상세정보 확인창 작업중
 */
public class MessageActivity extends AppCompatActivity {
    // 초기화해야하는 리스트를 초기화하는 곳


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (android.R.id.home): {
                finish();
                break;
            }
        }
        return true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content);
        Toolbar t = findViewById(R.id.message_bar);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("제목 히히");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView titleString = (TextView) findViewById(R.id.message_content_title);
        final TextView memoString = (TextView) findViewById(R.id.message_content_memo);
        final TextView dateString = (TextView) findViewById(R.id.message_content_time);

        //final TextView itemString = (TextView)v.findViewById(R.id.)
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm");

        if (getIntent() != null) {
//            Bundle bundle = getArguments();
//            int a = bundle.getInt("position");
//
//            System.out.println("bundle is =" +bundle);

            int a = getIntent().getIntExtra("position", 0);

            String getSubject = readList.get(a).component1().split("<")[0]; // subject
            String getFrom = readList.get(a).component2(); // from
            Date getDate = readList.get(a).component3(); // date
            // ImageView getimage= readList.get(a).component4();
            //System.out.println("subject printing... "+ getSubject.toString());

            String mTime = df.format(getDate);

//            // 추가 함
//
//            DataHandler dataHandler = bundle.getDataHandler();
//            String contentType = dataHandler.getContentType();
//
//            if(contentType.indexOf("multipart/mixed") != -1){
//                Multipart multipart = (Multipart)msg.getContent();// javax.mail.internet.MimeMultipart@8d463af
//                try{
//                    //System.out.println("multipart.getCount():"+multipart.getCount());
//                    for(int j=0;j<multipart.getCount();j++){
//                        BodyPart part = multipart.getBodyPart(j);
//                        //System.out.println(part.getContent());
//                        String disp = part.getDisposition();
//                        if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)){
//                            //첨부 파일 이라는 얘기임
//
//                            String filename =part.getFileName();
//
//                            System.out.println("첨부된 파일 이름 ="+filename);
//
////                                if (filename.startsWith("=?ks_c_5601-1987?B?")) //BASE64 로 인코딩 되있는 경우
////                                {
////                                    filename = filename.substring(19) ;
////                                    if (filename.indexOf('?') != -1) filename = filename.substring( 0 ,filename.indexOf('?') ) ;
////                                    filename = new String(MimeUtility.decodeText(filename)) ;
////                                }else if(filename.startsWith("=?ISO-8859-1?B?")){
////                                    filename = filename.substring(15) ;
////                                    if (filename.indexOf('?') != -1) filename = filename.substring( 0 ,filename.indexOf('?') ) ;
//                            filename = new String(MimeUtility.decodeText(filename)) ;
//                            Log.v("파일명 ","파일명:"+filename);
////                                }
//
//
//                            try{
//
//                                System.out.println("첨부된 파일 이름[인코딩]:"+ filename);
//                                File f = new File("C:"+File.separator+"Temp"+File.separator+"mail"+File.separator+filename);
//                                FileOutputStream fos = new FileOutputStream(f);
//
//                                byte[] buffer = new byte[1024];
//
//                                BufferedInputStream in = new BufferedInputStream(part.getInputStream());
//                                Log.v("여기야!! InputStream = ",in.toString());
//
//                                int n = 0;
//                                while((n=in.read(buffer, 0, 1024)) != -1) {
//                                    fos.write(buffer, 0, n);
//                                }
//                                fos.close();
//                                in.close();
//                            }catch(Exception e1){
//                                System.out.println("[SUB_ERROR]:"+e1.getMessage());
//                            }
//                        }//end attech
//
//
//                        //System.out.println("["+disp+"]"+part.getContentType());
//                    }
//                }catch(Exception e){
//                    System.out.println("[ERROR]:"+e.getMessage());
//                }
//            }
//
//
//            if ( (contentType.indexOf("text/html") != -1) || (contentType.indexOf("text/plain") != -1) ) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(dataHandler.getInputStream()));
////                    char[] buff = new char[512];
////                    int len;
////                    while ( (len = br.read(buff)) != -1) {
////                        System.out.print(new String(buff, 0, len));
////                    }
//                Log.v("여기야!! reading html...",br.toString());
//                br.close();
//            }
//            System.out.println("\n=====================================================\n");


            dateString.setText(mTime);
            titleString.setText(getSubject);
            memoString.setText(getFrom);
            //   imageView.setImageURI();
        }
    }

    // 레이아웃을 inflate 하고 각 리소스를 초기화하는 곳

//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_content,container,false);
//
//
//
//        return v;
//    }

    public void onBackPressed() {
        finish(); //액티비티 종료
//       if(getFragmentManager().getBackStackEntryCount() > 0){
//           getFragmentManager().popBackStack();
//       }
    }
}

