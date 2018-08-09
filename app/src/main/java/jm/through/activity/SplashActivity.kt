package jm.through.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("state", "launch") //state 키, launch값
        startActivity(intent)
        finish()
    }
}
