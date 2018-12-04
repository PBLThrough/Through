package jm.through.network

import jm.through.data.SignInData
import jm.through.data.SignInResult
import jm.through.data.SignUpResult
import jm.through.data.SignUpData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {
    //회원가입 -> 완료
    @POST("signup")
    fun signUp(@Body signUpData: SignUpData): Call<SignUpResult>

    //로그인 -> 완료
    @POST("signin")
    fun signIn(@Body signInData: SignInData): Call<SignInResult>

//    //쓰루유저의 이메일 계정 추가
//    @POST("addAccount")
//    fun addAccount(@Body addAccountData: AddAccountData): Call<addAccountResult>


}