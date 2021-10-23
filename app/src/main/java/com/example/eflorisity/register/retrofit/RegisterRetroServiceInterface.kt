package com.example.eflorisity.register.retrofit

import com.example.eflorisity.login.data.Email
import com.example.eflorisity.login.data.EmailResponse
import com.example.eflorisity.login.data.MemberDetails
import com.example.eflorisity.login.data.RegisterMemberResponse
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
}