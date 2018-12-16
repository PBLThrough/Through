package jm.through.network

import jm.through.data.*
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

    //쓰루유저의 이메일 계정 추가 -> 아마도 완료
    @POST("addAccount")
    fun addAccount(@Body addAccountData: AddAccountData): Call<AddAccountResult>

    //메일 정상 발송 시 신뢰 리스트 추가 -> 아마도 완료
    @POST("push")
    fun addTrustList(@Body addTrustData: AddTrustData): Call<AddTrustResult>

    //신뢰 이메일 리스트 보기 -> 안써도 됨
    @POST("list")
    fun showTrustList(@Body trustData: TrustData): Call<TrustResult>

    //소셜 계정 삭제 -> 완료 (status 코드 이상하게 들어옴, 삭제는 됨)
    @POST("removeAccount")
    fun removeAccount(@Body removeAccountData: RemoveAccountData): Call<RemoveAccountResult>

    @POST("pullOne")
    fun deleteTrust(@Body deleteTrustData: DeleteTrustData): Call<DeleteTrustResult>

}