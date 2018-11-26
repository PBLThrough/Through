package jm.through.activity

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pchmn.EmailValidator
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.fragment.AddDialogFragment
import jm.through.function.Authenticator
import jm.through.function.Authenticator.count
import jm.through.data.DetailData
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

        //host에 따라 다른 image 띄우기
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


        //인증 버튼 눌렀을 시
        authen_btn.setOnClickListener {
            id = email_edit.text.toString()
            pass = pass_edit.text.toString()

            //비어있는지 체크
            if (id == "" || pass == "") {
                Toast.makeText(this, "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else if (!validate(id)) {
                Toast.makeText(this, "올바른 이메일 패턴이 아닙니다", Toast.LENGTH_SHORT).show()
            } else {
                //dream7739@naver.com일시, host는 naver.com
                //.net인 경우도 있어서 통으로 저장
                host = id.split("@")[1]
                var task = Authentication()
                task.execute()
            }
        }


    }

    private fun validate(id: String): Boolean {
        val ev = EmailValidator()
        return ev.validateEmail(id)
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

            //계정 인증에 성공했을 시 -> 저장
            if (check) {

                //새로운 게정 정보
                val newAccount = DetailData(host, id, pass, count)

                //중복처리
                if (accountList.contains(newAccount)) {
                    val dialog = AddDialogFragment()

                    var bundle = Bundle()
                    bundle.putParcelable("newData", newAccount)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "중복 다이얼로그")
                } else {
                    accountList.add(newAccount)
                    var intent = Intent(applicationContext, MailActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            } else {
                Toast.makeText(this@AddAccountActivity, "계정 인증 실패", Toast.LENGTH_SHORT).show()
            }


        }

        override fun doInBackground(vararg params: Void?): Void? {

            //인증 실행
            var authenticator = Authenticator()
            check = authenticator.authen(host, id, pass)
            return null
        }
    }

    override fun onBackPressed() {
        //뒤로가기 버튼 눌렀을 시
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
