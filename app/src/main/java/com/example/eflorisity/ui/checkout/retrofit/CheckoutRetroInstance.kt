package com.example.eflorisity.ui.checkout.retrofit

import com.example.eflorisity.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CheckoutRetroInstance {
    companion object{
        val URL = Config()
        val BASE_URL = "http://${URL.baseUrl}/api/mobile/order_products/"

        fun getInstance(): Retrofit {
            val client = OkHttpClient.Builder()
            client.readTimeout(1, TimeUnit.MINUTES)
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}