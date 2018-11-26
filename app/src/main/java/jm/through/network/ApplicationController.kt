package jm.through.network

import android.app.Application
import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationController : Application() {

    //자유롭게 사용가능
    var networkService: NetworkService? = null
        private set   //set은 내부에서만 가능 = private set (Setter)

    override fun onCreate() {
        super.onCreate()

        context = this

        ApplicationController.instance = this //인스턴스 객체 초기화
        buildService()
    }

    fun buildService() {
        val builder = Retrofit.Builder()
        val retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        networkService = retrofit.create<NetworkService>(NetworkService::class.java!!)
    }



    /**static**/
    companion object {

        var instance: ApplicationController? = null
            private set

        private val baseUrl = "http://18.221.196.251:3000/"  // 베이스 url 초기화

        var context: Context? = null
            private set
    }

}