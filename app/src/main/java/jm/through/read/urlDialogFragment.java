package jm.through.read;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import org.jetbrains.annotations.Nullable;
import jm.through.R;


public class urlDialogFragment extends DialogFragment {
    static String url;
    static boolean flag = false;
    //각종 뷰 변수 선언
    ImageView web_image_Imageview;
    TextView web_title_textview;
    TextView web_contents_textview;
    TextView web_url_textview;
    Button web_yesbtn_button;
    Button web_nobtn_button;
    Context context;

    TextCrawler textCrawler = new TextCrawler();
    Bitmap[] currentImageSet;
    Bitmap currentImage;


    //Context받기 어케하는지 몰라~~ 번들로 받으셈
    public urlDialogFragment getURLFragment(){
        urlDialogFragment d = new urlDialogFragment();
        Bundle args = new Bundle();
        args.putInt("index",getIndex());
        d.setArguments(args);

        return d;
    }

    public int getIndex(){
        return getArguments().getInt("index",0);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        web_yesbtn_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                textCrawler
                        .makePreview(callback, url);
                Log.v("dialogFG","open url");
                flag = true;
            }
        });

        web_nobtn_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.v("dialogFG","close url");
                flag = false;
            }
        });
    }

    /**
     * mWebViewClient 에서 url을 가져오기
     * */
    public static void getURL(String text){
        url = text;
    }

    public static boolean checkFlag(){
        if (flag = true) // 네
            return true;
        else // 아니요
            return false;
    }

    public interface urlDialogFragmentListener{
        public void urlDialogClick(DialogFragment dialog, String someData);
    }

    urlDialogFragmentListener urlListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            urlListener = (urlDialogFragmentListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement urlListener");
        }
    }


//    public View OnCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
//        View view = inflater.inflate(R.layout.dialogfragment, container,false);
//
//        web_image_Imageview = view.findViewById(R.id.web_image);
//        web_title_textview = view.findViewById(R.id.web_title);
//        web_contents_textview = view.findViewById(R.id.web_contents);
//        web_url_textview = view.findViewById(R.id.web_url);
//        web_yesbtn_button = view.findViewById(R.id.web_btn_yes);
//        web_nobtn_button = view.findViewById(R.id.web_btn_no);
//
//        web_yesbtn_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                textCrawler
//                        .makePreview(callback, url);
//                Log.v("dialogFG","open url");
//                flag = true;
//            }
//        });
//
//        web_nobtn_button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Log.v("dialogFG","close url");
//                flag = false;
//            }
//        });
//        return view;
//    }

    private LinkPreviewCallback callback = new LinkPreviewCallback() {
        private View dialogView;
        private ImageView imageView;
        private LinearLayout linearLayout;

        /**
         * 뷰를 업데이트 하는 곳. 전부 커스터마이즈 해야함
         * onPre()는 crawling하기 전에 불려지고 onPos()는 그 이후에 불려짐
         * 뷰를 업데이트하기 위해서 이 뷰를 수정할 수 있음
         * */
        @Override
        public void onPre() {
            // preview를 만들기 전에 해아하는 것으로 보통 커스텀 preview layout을 설정함
            //dialogView = getLayoutInflater().inflate(R.layout.dialogfragment,null);
            linearLayout = dialogView.findViewById(R.id.external);
            web_url_textview.setText(TextCrawler.extendedTrim(url));

        }

        @Override
        public void onPos(SourceContent sourceContent, boolean b) {
            //sourceContent 결과로 미리보기 레이아웃을 채우기?
        }

//        @Override
//        public void onPos(SourceContent sourceContent, boolean b) {
//            linearLayout.removeAllViews();
//
//            if (b || sourceContent.getFinalUrl().equals("")){
//                View failed = getLayoutInflater().inflate(R.layout.failed,linearLayout);
//
//                TextView titleTextView = failed.findViewById(R.id.text);
//                titleTextView.setText("Preview failed.\nClick to dismiss.\n"+sourceContent.getFinalUrl());
//                failed.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //뒤로가기
//                    }
//                });
//            } else {
//                currentImageSet = new Bitmap[sourceContent.getImages().size()];
//
//                View content = getLayoutInflater().inflate(R.layout.dialogfragment,linearLayout);
//
//            }
//        }
    };

}

