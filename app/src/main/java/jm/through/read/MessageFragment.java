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
import java.text.ParseException;
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

        //final TextView itemString = (TextView)v.findViewById(R.id.)
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd EE요일, aa hh:mm");

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int a = bundle.getInt("position");

            String getSubject = readList.get(a).component1().split("<")[0]; // subject
            String getFrom = readList.get(a).component2(); // from
            Date getDate = readList.get(a).component3(); // date

            String mTime = df.format(getDate);

            dateString.setText(mTime);
            titleString.setText(getSubject);
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

