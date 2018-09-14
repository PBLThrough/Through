package jm.through.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import jm.through.R
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permission

class MainActivity : AppCompatActivity() {

    //퍼미션을 위한 변수
    private var REQUEST_STORAGE = 1
    private var PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v("Loading.. ","MainActivity!");

        //sharedpreference 기능
        val sp: SharedPreferences? = getSharedPreferences("sp", Context.MODE_PRIVATE)
        var sp_id: String = ""
        var sp_pwd: String = ""

        //이미 회원가입이 되어있을 시
        if (sp != null) {
            sp_id = sp?.getString("id", "")
            sp_pwd = sp?.getString("pwd", "")
            edit_id.setText(sp_id)
            edit_pwd.setText(sp_pwd)
        }

        //권한 요청
        if(!(checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)&&
                checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))){
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }


        //sign버튼 눌렀을 때
        btn_sign.setOnClickListener {
            var intent = Intent(applicationContext, SignActivity::class.java)
            startActivity(intent)
            finish()
        }


        //login버튼 눌렀을 때
        btn_login.setOnClickListener {
            //id값을 edit에서 가져옴
            var id = edit_id.text.toString()
            var pwd = edit_pwd.text.toString()

            if (id != "" && pwd != "") {
                if (id == sp_id && pwd == sp_pwd) {
                    var intent = Intent(applicationContext, MailActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "아이디나 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }



    //퍼미션을 체크하는 함수, 유저가 권한을 가지고 있을 경우 반환되는 값은 PackageManager.PERMISSION_GRANTED
     fun checkPermission(activity: Activity, permission: String): Boolean {

        var permissionResult:Int = ActivityCompat.checkSelfPermission(activity, permission)

        return permissionResult == PackageManager.PERMISSION_GRANTED //true, false 반환
    }

//    fun requestPermission(activity:Activity, permissions:Array<String>, requestCode:Int){
//        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_STORAGE)
//    }

    /**뒤로가기키 메인에서 누를 시 다이얼로그 출력**/
    override fun onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() > 0){
//            getFragmentManager().popBackStack();
//        }else {
//            super.onBackPressed()
//        }
        Log.v("MainActivity : ","onBackPressed Called")

        Toast.makeText(applicationContext,"메인 뒤로가기",Toast.LENGTH_SHORT);
        super.onBackPressed();

        Toast.makeText(applicationContext,"종료",Toast.LENGTH_SHORT)
        finish()

//

//        @Override
//        public void onBackPressed() {
//            if ( pressedTime == 0 ) {
//                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
//                pressedTime = System.currentTimeMillis();
//            }
//            else {
//                int seconds = (int) (System.currentTimeMillis() - pressedTime);
//
//                if ( seconds > 2000 ) {
//                    Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
//                    pressedTime = 0 ;
//                }
//                else {
//                    super.onBackPressed();
////                finish(); // app 종료 시키기
//                }
//            }
//        }


       
    }


}