package jm.through.read;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sun.mail.iap.Argument;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.mail.internet.MimeUtility;

import jm.through.R;

import static jm.through.read.MailReader.readList;

/**
 * 메일 상세정보 확인창 작업중
 * */
public class MessageFragment extends Fragment {
    // 초기화해야하는 리스트를 초기화하는 곳
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 레이아웃을 inflate 하고 각 리소스를 초기화하는 곳
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content,container,false);

        final TextView titleString = (TextView)v.findViewById(R.id.message_content_title);
        final TextView memoString = (TextView)v.findViewById(R.id.message_content_memo);
        final TextView dateString = (TextView)v.findViewById(R.id.message_content_time);

//        Date date = new Date();
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yy.MM.dd");
//        String mTime = mSimpleDateFormat.format(date);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int a = bundle.getInt("position");

            String getSubject = readList.get(a).component1().split("<")[0]; // subject
            String getFrom = readList.get(a).component2(); // from
            String getDate = readList.get(a).component3(); // date

            if (getDate != "null") {
                String getDate_date = getDate.split(" ")[3]; // 시간
                int getDate_date_time = Integer.parseInt(getDate_date.substring(0, 2));
                String getDate_date_behind = getDate_date.substring(2, 8);

                // 오전, 오후 변환
                if (getDate_date_time >= 12 && getDate_date_time <= 24) {
                    getDate_date_time -= 12;
                    getDate_date = ", 오후 " + getDate_date_time + getDate_date_behind;
                } else if (getDate_date_time >= 0) {
                    getDate_date = ", 오전 " + getDate_date_time + getDate_date_behind;
                }
                getDate = getDate.substring(0, 9) + getDate_date;
            }
            else {
                getDate = "시간 정보 없음";
            }
//            SimpleDateFormat df = new SimpleDateFormat("EE M/d/yy"); // 금 8/17/18
//            Date dated = new Date();
//            //df.format(getDate);
//           // System.out.println("1 getDate = "+getDate);
//            System.out.println("1 dated = " +df.format(dated));


//            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
                //           String mTime = mSimpleDateFormat.format(getDate);
                //System.out.println("Getdate = "+getDate); // Thu Aug 16 08:21:11
                //         System.out.println("mTime = "+mTime);

                titleString.setText(getSubject);
                dateString.setText(getDate);
                memoString.setText(getFrom);
            }

        return v;
    }

    public void onBackPressed(){
       if(getFragmentManager().getBackStackEntryCount() > 0){
           getFragmentManager().popBackStack();
       }
    }
}

