package jm.through.activity

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import jm.through.AccountData

import jm.through.R
import jm.through.R.id.toolbar
import jm.through.UserData
import jm.through.adapter.SettingAdapter
import jm.through.data.RemoveAccountData
import jm.through.data.RemoveAccountResult
import jm.through.data.SignInData
import jm.through.data.SignInResult
import jm.through.network.ApplicationController
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var sAdapter: SettingAdapter

    //itemClick 시 이벤트
    override fun onClick(v: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "환경설정"

        sAdapter = SettingAdapter(this, AccountData.accountList)
        sAdapter.setOnItemClickListenr(this@SettingActivity)
        setting_recycler.adapter = sAdapter
        setting_recycler.layoutManager = LinearLayoutManager(this)

        account_add_layout.setOnClickListener {
            val intent = Intent(applicationContext, AccountActivity::class.java)
            startActivity(intent)

        }

    }

    fun removeItem(position: Int) {
        //임시 저장
        val data = AccountData.accountList[position]

        //로컬삭제
        AccountData.accountList.removeAt(position)
        sAdapter.notifyItemRemoved(position)

        //통신(디비에 값 삭제)
        val networkService = ApplicationController.instance!!.networkService
        val removeData = RemoveAccountData(data.id, data.pass, UserData.token)
        val removeCallBack = networkService!!.removeAccount(removeData)

        removeCallBack.enqueue(object : Callback<RemoveAccountResult> {
            override fun onResponse(call: Call<RemoveAccountResult>, response: Response<RemoveAccountResult>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.v("token", UserData.token)
                } else {
                    //TODO 서버랑 message가 안맞음
                    Toast.makeText(applicationContext, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RemoveAccountResult>, t: Throwable) {
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
