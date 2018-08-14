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
//        View v2 = inflater.inflate(R.layout.check_board_item,container,false);
        //RecyclerView checkRecycler = (RecyclerView) v2.findViewById(R.id.recycler);

        // 1. ReadData<> 가져와서 읽어야하는 부분?
        // 2. 화면 넘어가기전 클릭한 부분 데이터 가져와서 올리면 되는건데 안됨 (어떻게 해야할지 감이 안잡힘
        final TextView titleString = (TextView)v.findViewById(R.id.message_content_title);
        final TextView memoString = (TextView)v.findViewById(R.id.message_content_memo);

//        Bundle bundle = getArguments();
//        String bundle_title = bundle.getString("mailtitle");
//        String bundle_memo = bundle.getString("mailmemo");

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int a = bundle.getInt("idx");
            String d1 = (readList.get(a).component1());
            String d2 = readList.get(a).component2();

            try {
                titleString.setText(MimeUtility.decodeText(d1));
                memoString.setText(MimeUtility.decodeText(d2));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//
//            titleString.setText(d1);
//            memoString.setText(d2);

            Log.v("d1 = ",d1);

//            titleString.setText(bundle_title);
//            memoString.setText(bundle_memo);
        }
//        final TextView t =(TextView)v2.findViewById(R.id.text_sender);
//        final TextView m = (TextView)v2.findViewById(R.id.text_subject);


//        String i = getArguments().get("");
//        String m = getArguments().get();

//        TextView t = (TextView)v2.findViewById(R.id.text_sender);
//        TextView s = (TextView)v2.findViewById(R.id.text_subject);
//        v.setOnTouchListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                titleString.setText(t.getText());
//                memoString.setText(m.getText());
//            }
//        });
//
//        checkRecycler.getChildAdapterPosition(v).mailtitle = titleString;
//        checkRecycler.getChildAdapterPosition(v).mailcontent = memoString;

        return v;
    }
}
