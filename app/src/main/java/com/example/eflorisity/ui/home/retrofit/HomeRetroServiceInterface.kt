package com.example.eflorisity.login.retrofit


import com.example.eflorisity.ui.home.data.ProductCart
import com.example.eflorisity.ui.home.data.ProductCartResponse
import com.example.eflorisity.ui.home.data.ProductDetails
import retrofit2.Call
import retrofit2.http.*

interface HomeRetroServiceInterface {

    @GET("products")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getProducts(@Header("Authorization") token: String): Call<List<ProductDetails>>

    @POST("cart/add")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun addToCart(@Header("Authorization") token:String , @Body params: ProductCart): Call<ProductCartResponse>
}