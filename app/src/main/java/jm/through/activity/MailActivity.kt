package jm.through.activity

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import jm.through.read.ReadFragment
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import jm.through.R
import jm.through.form.FormActivity
import jm.through.navigation.NavRecycler
import jm.through.send.SendActivity
import kotlinx.android.synthetic.main.nav_header_mail.*
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import android.widget.Spinner
import jm.through.navigation.UserRecycler


class MailActivity : AppCompatActivity() {

    var backPressCloseHandler = BackPressCloseHandler(this);
    var check = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)


        toolbarSetting() //toolbar에 대한 설정
        navSetting()
        backPressCloseHandler = BackPressCloseHandler(this)

        send_fab.setOnClickListener {
            val intent = Intent(this, SendActivity::class.java)
            startActivity(intent)
        }



    }

    fun navSetting() {
        val navFragment = NavRecycler()
        val userFragment = UserRecycler()
        val fragmentManager = supportFragmentManager

        fragmentManager.beginTransaction().replace(R.id.menu_container, navFragment).commit()

        spin_btn.setOnClickListener {
            spin_btn.animate().rotation(spin_btn.rotation - 180).start() //현재 좌표에서 180도 회전
            if (check) {
                fragmentManager.beginTransaction().replace(R.id.menu_container, userFragment).commit()
                check = false
            } else {
                fragmentManager.beginTransaction().replace(R.id.menu_container, navFragment).commit()
                check = true
            }
        }



    }


    /**툴바&드로어 설정 -> 툴바의 메뉴 버튼에 서랍이 붙어있음*/
    fun toolbarSetting() {

        //toolbar를 actionbar처럼 사용
        setSupportActionBar(toolbar)

        //드로어 toolbar toggle에 붙이기
        val toggle = object : ActionBarDrawerToggle(this, drawer_layout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            //드로어가 닫힐 때 키보드를 넣어주는 메소드
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                var imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)

                //드로어의 기본 리스트는 메일확인, 휴지통있는 리스트
                val navFragment = NavRecycler()
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.menu_container, navFragment).commit()

            }

            //드로어가 열릴 때도 키보드를 넣어주어야 함.
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                var imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)


            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }


    /** 뒤로가기 시 드로어 닫기 */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            backPressCloseHandler.onBackPressed()

            Toast.makeText(applicationContext, "메일 뒤로가기", Toast.LENGTH_SHORT);

        }
    }


    /** 드로어 닫기 */
     fun closeDrawer() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    /**선택한 이메일 헤더에 반영*/
    fun selectEmail(email:String){
        val str = email.split("@") //0이 아이디, 1이 플랫폼 주소
        email_main_text.text = str[0]
        email_sub_text.text = "@"+str[1]
    }

}