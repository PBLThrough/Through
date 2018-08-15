package jm.through.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import jm.through.read.ReadFragment
import jm.through.send.SendFragment
import kotlinx.android.synthetic.main.activity_mail.*
import kotlinx.android.synthetic.main.app_bar_mail.*
import android.view.inputmethod.InputMethodManager
import jm.through.R


class MailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail)
        toolbarSetting() //toolbar에 대한 설정
        nav_view.setNavigationItemSelectedListener(this) //navigationView(서랍)에 클릭리스너 달기

    }

    //menu 파일 inflate
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    /**서랍 아이템 클릭 시*/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //서랍에 들어가는 아이템들 = 메뉴 형태, 인자를 보면 MenuItem인 것을 볼 수 있음.

        val id = item.itemId
        var send = SendFragment() //메일 보내는 fragment
        var read = ReadFragment() //메일 받는 fragment

        //menu 폴더 -> main.xml을 열어보면 id값들이 있음. 그 항목들의 id를 받아서
        //클릭 됬는지 알려준다.

        if (id == R.id.nav_camera) {
            var fm = fragmentManager //fragment교체에 필요한 fragmentManager
            fm.beginTransaction().replace(R.id.fragment_container, send).commit() //fragment_container를 activity_mail.xml에서 찾아보세용ㅎ_ㅎ
            toolbar.title = "메일 쓰기" //fragment_container를 SendFragment로 채워주고 commit해서 교체!, 툴바의 titled은 "메일 쓰기"로 바꿔준다.
        } else if (id == R.id.nav_gallery) {
            var fm = fragmentManager
            fm.beginTransaction().replace(R.id.fragment_container, read).commit()
            toolbar.title = "메일 확인"
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)  //클릭했으면 서랍이 닫히고 화면이 나와야 하니 이 코드가 필요~
        return true
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
            super.onBackPressed()
        }
    }
}