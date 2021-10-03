package com.example.eflorisity.login.retrofit

import com.example.eflorisity.login.data.MemberLoginDetails
import com.example.eflorisity.login.data.MemberResponse
import com.example.eflorisity.ui.home.data.ProductDetails
import retrofit2.Call
import retrofit2.http.*

interface HomeRetroServiceInterface {

    @GET("products")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getProducts(@Header("Authorization") token: String): Call<List<ProductDetails>>
}