package jm.through.account

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast

import jm.through.R
import jm.through.R.id.authen_btn
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_add_account.*

class AccountActivity : AppCompatActivity(), View.OnClickListener {

    var account_list = ArrayList<AccountData>()
    lateinit var aAdapter:AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        account_list.add(AccountData(R.drawable.naver,"naver"))
        account_list.add(AccountData(R.drawable.google,"gmail"))
        account_list.add(AccountData(R.drawable.daum,"daum"))

        aAdapter = AccountAdapter(account_list)
        account_recycler.adapter = aAdapter
        aAdapter.setOnItemClickListener(this)
        account_recycler.layoutManager = LinearLayoutManager(this)


    }





    override fun onClick(v: View?) {
        val idx =  account_recycler.getChildAdapterPosition(v!!)
        val addIntent = Intent(this, AddAccountActivity::class.java)

        var platformName = account_list.get(idx).platformName

        addIntent.putExtra("platformName",platformName)
        startActivity(addIntent)
        finish()
    }




}
