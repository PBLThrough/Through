package jm.through.read;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
 * */


public class MessageFragment extends Fragment {



//    private void animateToFragment(Fragment newFragment, String tag) {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_container, newFragment, tag);
//        ft.addToBackStack(null);
//        ft.commit();
//    }

    // 초기화해야하는 리스트를 초기화하는 곳
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//        final TextView titleString = (TextView)v.findViewById(R.id.message_content_title);
//        final TextView memoString = (TextView)v.findViewById(R.id.message_content_memo);
//        final TextView dateString = (TextView)v.findViewById(R.id.message_content_time);
//
//        //final TextView itemString = (TextView)v.findViewById(R.id.)
//        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm");
//
//        if (getArguments() != null) {
//            Bundle bundle = getArguments();
//            int a = bundle.getInt("position");
//            MailContentBuilder mailContentBuilder = new MailContentBuilder();
//
//            System.out.println("bundle is =" +bundle);
//
//            String getSubject = readList.get(a).component1().split("<")[0]; // subject
//            String getMemo = readList.get(a).component2(); // memo
//            Date getDate = readList.get(a).component3(); // date
////            Object getcontent = readList.get(a).component5();//content
//            // ImageView getimage= readList.get(a).component4();
//            //System.out.println("subject printing... "+ getSubject.toString());
//
//            String mTime = df.format(getDate);
//
//            dateString.setText(mTime);
//            titleString.setText(getSubject);
//
//            memoString.setText(getMemo); // + getcontent
//            //   imageView.setImageURI();
//        }


//        FragmentTransaction ft = FragmentManager.beginTransaction();
//
//        String backStateName = pFragment.getClass().getName();
//        int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
//
//        if(backStackEntryCount > 0){ //프래그먼트가 1개이상 존재할 경우
//            FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(backStackEntryCount-1);
//            String beforeBackStackName = backStackEntry.getName();
//            if(!backStateName.equals(beforeBackStackName)){ //이전 백스택과 현재 백스텍이 동일하다면 백스택을 추가하지않고, 동일하지 않다면 추가한다. (중복 스택생성 방지)
//                ft.replace(R.id.content_frame, pFragment);
//                ft.addToBackStack(backStateName);
//            }
//        }else{ //최초 프래그먼트 세팅시
//            ft.replace(R.id.content_frame, pFragment);
//            ft.addToBackStack(backStateName);
//        }
//        ft.commit();

    }

    // 레이아웃을 inflate 하고 각 리소스를 초기화하는 곳
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content,container,false);
        ReadFragment readfragment = new ReadFragment();
       // getFragmentManager().beginTransaction().replace(R.id.fragment_container,readfragment);


        final TextView titleString = (TextView)v.findViewById(R.id.message_content_title);
        final TextView memoString = (TextView)v.findViewById(R.id.message_content_memo);
        final TextView dateString = (TextView)v.findViewById(R.id.message_content_time);

        //final TextView itemString = (TextView)v.findViewById(R.id.)
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm");

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int a = bundle.getInt("position");
            MailContentBuilder mailContentBuilder = new MailContentBuilder();

            System.out.println("bundle is =" +bundle);

            String getSubject = readList.get(a).component1().split("<")[0]; // subject
            String getMemo = readList.get(a).component2(); // memo
            Date getDate = readList.get(a).component3(); // date
//            Object getcontent = readList.get(a).component5();//content
           // ImageView getimage= readList.get(a).component4();
            //System.out.println("subject printing... "+ getSubject.toString());

            String mTime = df.format(getDate);

            // 추가 함
//
//            DataHandler dataHandler = bundle.getDataHandler();
//            String contentType = dataHandler.getContentType();

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
            System.out.println("\n=====================================================\n");

            dateString.setText(mTime);
            titleString.setText(getSubject);

            memoString.setText(getMemo); // + getcontent
         //   imageView.setImageURI();
            }

        return v;
    }

}

