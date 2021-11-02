package com.example.eflorisity.ui.orders.retrofit

import com.example.eflorisity.ui.home.data.ProductDetails
import com.example.eflorisity.ui.orders.data.OrdersWithProduct
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface OrdersRetroServiceInterface {
    @GET("order_products/orders/{member_id}/{status}")
    @Headers("Accept: application/json","Content-Type: application/json")
    fun getOrders(@Path("member_id") member_id:String,@Path("status") status:Int): Call<ArrayList<OrdersWithProduct>>
}