package jm.through.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import jm.through.R
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        var platformName :String
        platformName = intent.getStringExtra("platformName")

        when(platformName){
            "naver" -> {
                add_image.setImageResource(R.drawable.naversmall)
            }
            "gmail" -> {
                add_image.setImageResource(R.drawable.googlesmall)
            }
            "daum" -> {
                add_image.setImageResource(R.drawable.daumsmall)
            }

        }

        authen_btn.setOnClickListener{
            //인증
        }

    }
}
