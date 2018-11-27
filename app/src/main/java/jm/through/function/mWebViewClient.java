package jm.through.function;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import jm.through.fragment.urlDialogFragment;
import com.leocardz.link.preview.library.TextCrawler;


public class mWebViewClient extends WebViewClient {
    public boolean check = urlDialogFragment.checkFlag();

    /**
     * 처음 한번만 호출되는 메소드
     * 페이지 로딩이 시작된 것을 알림. 각각의 main frame이 iframe에 페이지를
     * 로드하기 위해 한번 호출하거나 frameset이 main frame에 대해 이 메소드를 한번
     * 호출할 것. 이 메소드가 임메디드 프래임 내용이 변경되었을 때 호추로디지 않는다는 것드 뜻함.
     * iframe이 있는 대상 링크를 클릭한 것
     * */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        urlDialogFragment.getURL(url);
        Log.v("webview","check is " + check);

        if(check == true){
            super.onPageStarted(view, url, favicon);
        }

    }

    // 리소스를 로드하는 중 여러번 호출
    /**
     * 페이지 로딩이 완료될 때까지 여러번 호출.
     * */
    @Override
    public void onLoadResource(WebView view, String url) {
        if(check == true) super.onLoadResource(view, url);
    }

    // 방문 내역을 히스토리에 업데이트 할 때
    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if(check == true) super.doUpdateVisitedHistory(view, url, isReload);
    }

    // 로딩이 완료됬을 때 한번 호출
    /**
     * 이 메소드는 메인 프레임에 대해서만 호출. picture rendering은 아직 업데이트되지 않을 수 있음
     * 새로운 picture가 있다는 사실을 알기 위해 onNewPicture(webview, picture) 메소드 사용
     * */
    @Override
    public void onPageFinished(WebView view, String url) {
        Log.v("webview","onPageFinished");
        if(check == true) super.onPageFinished(view, url);
    }

    // 오류가 났을 경우, 오류는 복수할 수 없음
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if(check == true) {
            super.onReceivedError(view, errorCode, description, failingUrl);


            switch (errorCode) {
                case ERROR_AUTHENTICATION:
                    break;               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:
                    break;                           // 잘못된 URL
                case ERROR_CONNECT:
                    break;                          // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:
                    break;    // SSL handshake 수행 실패
                case ERROR_FILE:
                    break;                                  // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:
                    break;               // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:
                    break;           // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:
                    break;                              // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:
                    break;   // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:
                    break;               // 너무 많은 리디렉션
                case ERROR_TIMEOUT:
                    break;                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:
                    break;     // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:
                    break;                        // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:
                    break; // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:
                    break;          // URI가 지원되지 않는 방식
            }
        }
    }

    // http 인증 요청이 있는 경우, 기본 동작은 요청 취소
    /**
     * 기본적으로 데이터를 재발송하지 않는 것
     * */
    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if(check == true) super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    // 확대나 크기 등의 변화가 있는 경우
    /**
     * Webview가 변화하기 위해 scale이 적용된다고 알림*/
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        if(check == true) super.onScaleChanged(view, oldScale, newScale);
    }

    // 잘못된 키 입력이 있는 경우
    /**
     * 호스트 응용프로그램에게 동기적으로 키 이벤트를 처리할 기회를 줍니다.
     * true를 반한활 경우, webview는 키 이벤트를 처리하지 않습니다.
     * false를 반환할 경우, webview는 항상 키 이벤트를 처리합니다.
     * 기본 동작은 false를 반환합니다.
     * */
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if(check == true) return super.shouldOverrideKeyEvent(view, event);
        else return false;
    }

    // 새로운 URL이 webview에 로드되려 할 경우 컨트롤을 대신할 기회를 줌
    /**
     * 새로운 url이 webview에 로드되려고 할 때 호스트 응용 프로그램에게 컨트롤을
     * 대신할 기회를 줍니다. webviewclient가 제공되지 않으면, 기본적으로 webview는 url에
     * 대한 적절한 핸들러를 선택하려고 activitiy manager에게 물어봄.
     * webviewclient가 제공되면 호스트 응용 프로그램이 url을 처리한다는 의미인 true를 반환하거나
     * 현재 webview가 url을 처리하는 의미인 false를 반환합니다.
     * */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v("webview","shouldOverrideUrlLoading");

       // Intent intent = new Intent(getC)
        /**
         * 여기서 다이얼로그 띄우고 url 넣어주기
         */
        if(check == true) {
            view.loadUrl(url);
            return true;
        }
        else return false;
    }

}

