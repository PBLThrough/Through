package jm.through.account

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.account.Authenticator.count
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccountActivity : AppCompatActivity() {
    var id: String = ""
    var pass: String = ""
    var host: String = ""
    var check: Boolean = false


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
            "hanmail" -> {
                add_image.setImageResource(R.drawable.daumsmall)
            }
            "nate" -> {
                add_image.setImageResource(R.drawable.natesmall)
            }
            "yahoo" -> {
                add_image.setImageResource(R.drawable.yahoomail)
            }
            "etc" -> {
                //교체
                add_image.setImageResource(R.drawable.send)
            }

        }

        authen_btn.setOnClickListener {
            id = email_edit.text.toString()
            pass = pass_edit.text.toString()

            if (id == "" || pass == "") {
                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else {
                var task = Authentication()
                task.execute()
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

            if (check) {

                //id가 @를 포함하고 있지 않을 시 이메일 포맷으로 변경
                if (!id.contains("@")) {
                    id = "$id@$host.com"
                }

                val newAccount = DetailData(host, id, pass, count) //새로운 게정 정보

                //중복처리
                if (accountList.contains(newAccount)) {
                    val dialog = AddDialogFragment()

                    var bundle = Bundle()
                    bundle.putParcelable("newData",newAccount)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "중복 다이얼로그")
                } else {
                    accountList.add(newAccount)
                    finish()
                }


            } else {
                Toast.makeText(this@AddAccountActivity, "계정 인증 실패", Toast.LENGTH_SHORT).show()
            }


        }

        override fun doInBackground(vararg params: Void?): Void? {
            var authenticator = Authenticator()
            check = authenticator.authen(host, id, pass)
            return null
        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, AccountActivity::class.java)
        startActivity(intent)
        finish()

    }

    //finish X 가려져서 보이지 않을 때, OnPause(다른엑티비티 활성화되기 직전, 화면이 조금이라도 남아있음)다음으로 실행
    override fun onStop() {
        super.onStop()
    }

    //finish가 되어서 아예 activity가 파괴
    override fun onDestroy() {
        super.onDestroy()
    }

}
