package jm.through.activity

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pchmn.EmailValidator
import jm.through.AccountData.accountList
import jm.through.R
import jm.through.UserData
import jm.through.data.*
import jm.through.fragment.AddDialogFragment
import jm.through.function.Authenticator
import jm.through.function.Authenticator.count
import jm.through.network.ApplicationController
import kotlinx.android.synthetic.main.activity_add_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

                //서버용
                val serverAccount = AddAccountData(id, pass, UserData.token)

                //로컬용
                val localAccount = DetailData(host, id, pass, count)


                //중복처리
                if (accountList.contains(localAccount)) {
                    //중복이면 저장불가하게 막기(이게 편할 것 같음)
                    val dialog = AddDialogFragment()
                    var bundle = Bundle()
                    bundle.putParcelable("newData", localAccount)
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager, "중복 다이얼로그")
                } else {
                    //중복이 아니면 서버 통신
                    addAccount(serverAccount,localAccount)
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

    fun addAccount(data1: AddAccountData, data2:DetailData) {
        // accountList.add()

        val networkService = ApplicationController.instance!!.networkService
        val addAccountData = data1
        val addAccountCallback = networkService!!.addAccount(addAccountData)

        //서버에 계정을 넣어주고 업데이트는 로컬로만 함(로컬 detailData)
        addAccountCallback.enqueue(object : Callback<AddAccountResult> {
            override fun onResponse(call: Call<AddAccountResult>, response: Response<AddAccountResult>) {
                if (response.isSuccessful) {
                    //로컬에 저장하고 나중에 로그인 다시하면 서버에 저장되게
                    Log.v("serversendEmail",data1.toString())
                    accountList.add(data2)
                    Toast.makeText(applicationContext, "계정 추가 완료", Toast.LENGTH_SHORT).show()
                    var intent = Intent(ApplicationController.context,  MailActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<AddAccountResult>, t: Throwable) {
                Toast.makeText(ApplicationController.context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        })


        var intent = Intent(applicationContext, MailActivity::class.java)
        startActivity(intent)
        finish()
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
