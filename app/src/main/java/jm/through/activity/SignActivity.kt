package jm.through.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.widget.Toast
import jm.through.R
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val sp: SharedPreferences? = getSharedPreferences("sp", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sp!!.edit()


        sign_button.setOnClickListener {
            var name = sign_name.text.toString()
            var id = sign_id.text.toString()
            var pwd = sign_pwd.text.toString()

            if (name == "" || id == "" || pwd == "") {
                Toast.makeText(applicationContext, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else {
                editor?.putString("name", name)
                editor?.putString("id", id)
                editor?.putString("pwd", pwd)
                editor?.commit() //내용 반영
                Toast.makeText(applicationContext, "회원 가입 완료", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


    /**뒤로가기 시 로그인으로*/
    override fun onBackPressed() {
        var intent=Intent(this,MainActivity::class.java)
        Toast.makeText(applicationContext,"사인 뒤로가기",Toast.LENGTH_SHORT);
        startActivity(intent)
        finish() //종료
    }

}
