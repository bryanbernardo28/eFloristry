package com.example.eflorisity.register.retrofit

import com.example.eflorisity.login.data.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RegisterRetroServiceInterface {
    @POST("register/create")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun registerMember(@Body params: MemberDetails): Call<RegisterMemberResponse>

    @POST("register/resend_verification")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun resendVerificationCode(@Body params: Email): Call<EmailResponse>

    @POST("register/submit_verification")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun submitVerificationCode(@Body params: Code): Call<CodeResponse>
}