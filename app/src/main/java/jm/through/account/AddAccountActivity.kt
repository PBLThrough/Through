package jm.through.account

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast

import jm.through.R
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccountActivity : AppCompatActivity() {
    var id: String = ""
    var pass: String = ""
    var host: String = ""
    var check:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        host = intent.getStringExtra("platformName")

        when (host) {
            "naver" -> {
                add_image.setImageResource(R.drawable.naversmall)
            }
            "gmail" -> {
                add_image.setImageResource(R.drawable.googlesmall)
            }
            "daum" -> {
                add_image.setImageResource(R.drawable.daumsmall)
            }

        }

        authen_btn.setOnClickListener {
            id = email_edit.text.toString()
            pass = pass_edit.text.toString()

            if (id == "" || pass == "") {
                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else {
                var authenTask = Authentication()
                authenTask.execute()
            }
        }

    }


    inner class Authentication : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            authen_progress.visibility = View.VISIBLE
        }

        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            authen_progress.visibility = View.INVISIBLE

            //dialog로 변경
            if(check){
                Toast.makeText(this@AddAccountActivity, "계정 인증 성공",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@AddAccountActivity, "계정 인증 실패",Toast.LENGTH_SHORT).show()
            }

        }

        override fun doInBackground(vararg params: Void?): Void? {
            var authenticator = Authenticator()
            check = authenticator.authen(host,id,pass)
            return null
        }
    }


}
