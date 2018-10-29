package jm.through.account

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import jm.through.R
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity(), View.OnClickListener {

    var account_list = ArrayList<PlatformData>()
    lateinit var aAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        if (account_list.isEmpty()) {
            account_list.add(PlatformData(R.drawable.naver, "naver"))
            account_list.add(PlatformData(R.drawable.google, "gmail"))
            account_list.add(PlatformData(R.drawable.daum, "hanmail"))
            account_list.add(PlatformData(R.drawable.natemail, "nate"))
            account_list.add(PlatformData(R.drawable.yahoomail, "yahoo"))
            account_list.add(PlatformData(R.drawable.send, "etc"))
        }

        aAdapter = AccountAdapter(account_list)
        account_recycler.adapter = aAdapter
        aAdapter.setOnItemClickListener(this)
        account_recycler.layoutManager = LinearLayoutManager(this)
    }


    override fun onClick(v: View?) {
        val idx = account_recycler.getChildAdapterPosition(v!!)
        val addIntent = Intent(this, AddAccountActivity::class.java)

        var platformName = account_list.get(idx).platformName

        addIntent.putExtra("platformName", platformName)
        startActivity(addIntent)
        finish()
    }


}
