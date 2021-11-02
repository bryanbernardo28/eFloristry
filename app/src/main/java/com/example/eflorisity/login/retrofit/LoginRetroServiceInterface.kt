package com.example.eflorisity.login.retrofit

import com.example.eflorisity.login.data.*
import retrofit2.Call
import retrofit2.http.*

interface LoginRetroServiceInterface {
    @POST("login/login_submit")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun loginMember(@Body params: MemberLoginDetails): Call<MemberResponse>
}