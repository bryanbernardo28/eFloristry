package com.example.eflorisity.ui.cart.retrofit

import com.example.eflorisity.ui.cart.data.Cart
import com.example.eflorisity.ui.cart.data.DeleteItemFromCartResponse
import com.example.eflorisity.ui.checkout.data.Checkout
import com.example.eflorisity.ui.home.data.ProductCartListNotIn
import com.example.eflorisity.ui.home.data.ProductDetails
import retrofit2.Call
import retrofit2.http.*

interface CartRetroServiceInterface {
    @GET("cart/myCart/{id}")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getMyCart(@Path("id") id: String): Call<ArrayList<Cart>>

    @POST("cart/myCartNotIn")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getMyCartNotIn(@Body params: ProductCartListNotIn): Call<ArrayList<Cart>>

    @DELETE("cart/delete/{member_id}/{id}")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun deleteFromMyCart(@Path("member_id") member_id:String,@Path("id") id: String): Call<DeleteItemFromCartResponse>
}