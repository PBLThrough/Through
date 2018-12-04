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
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import jm.through.AccountData
import jm.through.R
import jm.through.UserData
import jm.through.adapter.ReadAdapter
import jm.through.data.*
import jm.through.data.SignInResult.SocialEmailData
import jm.through.function.Authenticator
import jm.through.function.Authenticator.count
import jm.through.function.FolderFetchImap
import jm.through.network.ApplicationController
import jm.through.network.ApplicationController.Companion.context
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_check.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        Log.v("Loading.. ", "MainActivity!");

        //sharedpreference 기능
        val sp: SharedPreferences? = getSharedPreferences("sp", Context.MODE_PRIVATE)
        var sp_id: String = ""
        var sp_pwd: String = ""

        //이미 회원가입이 되어있을 시
        if (sp != null) {
            sp_id = sp?.getString("id", "")
            sp_pwd = sp?.getString("passwd", "")
            edit_id.setText(sp_id)
            edit_pwd.setText(sp_pwd)
        }

        //권한 요청
        if (!(checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
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

            signIn(id, pwd)

        }
    }

    fun signIn(id: String, pwd: String) {

        if (id != "" && pwd != "") {
            val networkService = ApplicationController.instance!!.networkService
            val signInData = SignInData(id, pwd)
            val signInCallBack = networkService!!.signIn(signInData)

            signInCallBack.enqueue(object : Callback<SignInResult> {
                override fun onResponse(call: Call<SignInResult>, response: Response<SignInResult>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        signInSetting(response.body())
                        Log.v("token",UserData.token)
                    }
                }

                override fun onFailure(call: Call<SignInResult>, t: Throwable) {
                    Toast.makeText(ApplicationController.context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
                }

            })
        } else {
            Toast.makeText(applicationContext, "아이디와 패스워드를 입력하세요", Toast.LENGTH_SHORT).show()
        }

    }

    fun signInSetting(body: SignInResult?) {

        //토큰 설정
        UserData.token = body!!.token //토큰 세팅

        if (body.SocialEmailList.isEmpty()) {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        } else {
            //신뢰할 수 있는 리스트 설정
            UserData.trustList = body!!.EmailList
            AccountData.accountList = ArrayList()

            //소셜 계정
            for (data in body!!.SocialEmailList) {
                val platform = data.email.split("@")[1]
                val email = data.email
                var passwd = data.passwd

                val count = getCount(platform, email, passwd)
                var detailData = DetailData(platform, email, passwd, count)
                AccountData.accountList.add(detailData)
            }

            var intent = Intent(context, MailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCount(platform: String, email: String, passwd: String): Int {

        var authentication = Authenticator()

        val t = Thread(Runnable {
            authentication.authen(platform, email, passwd)
        })

        t.start()

        while(t.isAlive){}

        return count

    }


    //퍼미션을 체크하는 함수, 유저가 권한을 가지고 있을 경우 반환되는 값은 PackageManager.PERMISSION_GRANTED
    fun checkPermission(activity: Activity, permission: String): Boolean {

        var permissionResult: Int = ActivityCompat.checkSelfPermission(activity, permission)

        return permissionResult == PackageManager.PERMISSION_GRANTED //true, false 반환
    }


    /**뒤로가기키 메인에서 누를 시 다이얼로그 출력**/
    override fun onBackPressed() {

        Log.v("MainActivity : ", "onBackPressed Called")

        Toast.makeText(applicationContext, "메인 뒤로가기", Toast.LENGTH_SHORT);
        super.onBackPressed();

        Toast.makeText(applicationContext, "종료", Toast.LENGTH_SHORT)
        finish()


    }


}