package com.example.eflorisity.ui.checkout.retrofit

import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.checkout.data.CheckoutResponse
import com.example.eflorisity.ui.checkout.data.GetShippingFee
import com.example.eflorisity.ui.checkout.data.ShippingFeeResponse
import com.example.eflorisity.ui.home.data.ProductDetails
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface CheckoutRetroServiceInterface {

    @POST("order_products/add_order_products")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun addOrderProducts(@Header("Authorization") token: String,@Body params: Checkout): Call<CheckoutResponse>

    @GET("states")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getStates(): Call<JsonObject?>

    @POST("shippingFee")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getShippingFee(@Body params: GetShippingFee): Call<String?>
}