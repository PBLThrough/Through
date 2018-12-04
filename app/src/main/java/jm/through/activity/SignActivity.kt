package jm.through.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.widget.Toast
import jm.through.AccountData
import jm.through.R
import jm.through.data.DetailData
import jm.through.data.SignInData
import jm.through.data.SignUpData
import jm.through.data.SignUpResult
import jm.through.network.ApplicationController
import jm.through.network.ApplicationController.Companion.context
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.activity_sign.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignActivity : AppCompatActivity() {

    lateinit var sp: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var signUpResult: SignUpResult

    var name: String = ""
    var id: String = ""
    var passwd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        sp = getSharedPreferences("sp", Context.MODE_PRIVATE)
        editor = sp!!.edit()

        //회원가입 버튼 클릭시
        sign_button.setOnClickListener {
            name = sign_name.text.toString()
            id = sign_id.text.toString()
            passwd = sign_pwd.text.toString()

            if (name == "" || id == "" || passwd == "") {
                Toast.makeText(applicationContext, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else {
                signUp()
            }


        }

    }


    fun signUp() {
        val networkService = ApplicationController.instance!!.networkService
        val signUpData = SignUpData(id, name, passwd)
        val signUpCallBack = networkService!!.signUp(signUpData)

        signUpCallBack.enqueue(object : Callback<SignUpResult> {
            override fun onResponse(call: Call<SignUpResult>, response: Response<SignUpResult>) {
                if (response.isSuccessful) {
                    editor?.putString("name", name)
                    editor?.putString("id", id)
                    editor?.putString("passwd", passwd)
                    editor?.commit() //내용 반영


                    Toast.makeText(applicationContext, "회원 가입 완료", Toast.LENGTH_SHORT).show()
                    var intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<SignUpResult>, t: Throwable) {
                Toast.makeText(context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        })


    }


    /**뒤로가기 시 로그인으로*/
    override fun onBackPressed() {
        var intent = Intent(this, MainActivity::class.java)
        Toast.makeText(applicationContext, "사인 뒤로가기", Toast.LENGTH_SHORT);
        startActivity(intent)
        finish() //종료
    }

}
