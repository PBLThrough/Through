package jm.through.read;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import jm.through.R;

import static jm.through.read.MailReader.readList;

/**
 * 메일 상세정보 확인창 작업중
 * */
public class MessageFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content,container,false);
        //View v2 = inflater.inflate(R.layout.check_board_item,container,false);
        //RecyclerView checkRecycler = (RecyclerView) v2.findViewById(R.id.recycler);

        TextView titleString =(TextView)v.findViewById(R.id.message_content_title);
        TextView memoString =(TextView)v.findViewById(R.id.message_content_memo);

        //TextView t = (TextView)v2.findViewById(R.id.text_sender);
        //TextView s = (TextView)v2.findViewById(R.id.text_subject);
//        checkRecycler.getChildAdapterPosition(v).mailtitle = titleString;
//        checkRecycler.getChildAdapterPosition(v).mailcontent = memoString;

        return v;
    }


}
