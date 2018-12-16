package jm.through.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import jm.through.R
import jm.through.R.id.trust_recycler
import jm.through.adapter.TrustAdapter
import kotlinx.android.synthetic.main.activity_trust.*
import kotlinx.android.synthetic.main.app_bar_mail.*

class TrustActivity : AppCompatActivity() {

    /**신뢰할 수 있는 사람 리스트를 보여줌*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trust)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "신뢰할 수 있는 수신자"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var tAdapter = TrustAdapter(this, jm.through.UserData.trustList)
        trust_recycler.adapter = tAdapter
        trust_recycler.layoutManager = LinearLayoutManager(this)
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
