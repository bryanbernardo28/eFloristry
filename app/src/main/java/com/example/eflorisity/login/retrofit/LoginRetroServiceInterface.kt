package com.example.eflorisity.login.retrofit

import com.example.eflorisity.login.data.MemberLoginDetails
import com.example.eflorisity.login.data.MemberResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRetroServiceInterface {

    @POST("login_submit")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun loginMember(@Body params: MemberLoginDetails): Call<MemberResponse>
}