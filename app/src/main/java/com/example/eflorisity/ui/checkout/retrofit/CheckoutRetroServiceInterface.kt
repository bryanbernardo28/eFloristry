package com.example.eflorisity.ui.checkout.retrofit

import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CheckoutRetroServiceInterface {

    @POST("order_products/add_order_products")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun addOrderProducts(@Header("Authorization") token: String,@Body params: Checkout): Call<CheckoutResponse>
}