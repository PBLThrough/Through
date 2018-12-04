package jm.through.activity

import android.content.Intent
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import jm.through.AccountData

import jm.through.R
import jm.through.R.id.toolbar
import jm.through.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.app_bar_mail.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    //itemClick 시 이벤트
    override fun onClick(v: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "환경설정"

        var sAdapter = SettingAdapter(this, AccountData.accountList)
        sAdapter.setOnItemClickListenr(this@SettingActivity)
        setting_recycler.adapter = sAdapter
        setting_recycler.layoutManager = LinearLayoutManager(this)

        account_add_layout.setOnClickListener{
            val intent = Intent(applicationContext, AccountActivity::class.java)
            startActivity(intent)

        }

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
