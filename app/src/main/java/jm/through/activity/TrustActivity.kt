package jm.through.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import jm.through.AccountData
import jm.through.R
import jm.through.R.id.trust_recycler
import jm.through.adapter.TrustAdapter
import jm.through.data.DeleteTrustData
import jm.through.data.DeleteTrustResult
import jm.through.data.RemoveAccountData
import jm.through.data.RemoveAccountResult
import jm.through.network.ApplicationController
import kotlinx.android.synthetic.main.activity_trust.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrustActivity : AppCompatActivity() {

    lateinit var tAdapter:TrustAdapter

    /**신뢰할 수 있는 사람 리스트를 보여줌*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trust)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "신뢰할 수 있는 수신자"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tAdapter = TrustAdapter(this, jm.through.UserData.trustList)
        trust_recycler.adapter = tAdapter
        trust_recycler.layoutManager = LinearLayoutManager(this)
    }

    fun removeItem(position:Int){
        val data = jm.through.UserData.trustList[position]

        //로컬삭제
        jm.through.UserData.trustList.removeAt(position)
        tAdapter.notifyItemRemoved(position)

        //통신(디비에 값 삭제)
        val networkService = ApplicationController.instance!!.networkService

        val removeData = DeleteTrustData(jm.through.UserData.token, data.email)
        val removeCallBack = networkService!!.deleteTrust(removeData)

        removeCallBack.enqueue(object : Callback<DeleteTrustResult> {
            override fun onResponse(call: Call<DeleteTrustResult>, response: Response<DeleteTrustResult>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.v("token", jm.through.UserData.token)
                } else {
                    Toast.makeText(applicationContext, "삭제 실패.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteTrustResult>, t: Throwable) {
                Toast.makeText(ApplicationController.context, "네트워크가 원할하지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                android.R.id.home -> {
                    finish()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
