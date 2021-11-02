package com.example.eflorisity.retrofit

import com.example.eflorisity.login.retrofit.BasicAuthenticationIntercepter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class PayPalBasicAuthInstance {
    companion object{
        val BASE_URL = "https://api-m.sandbox.paypal.com/"

        fun getInstance(): Retrofit {
            val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthenticationIntercepter("AYck3V8nQuePlPoEmhEoLM6WpvF9aV5cc8-mpziWeE8wMw1dn8lX2JhfKk-ISxm_YboZGQA4ppekKlw7","EFFtZpUdX41ZsrgAiLMDsr1YdzL8sygS8cR4bJKqEkwOnMofiUdF8gJLgvGPfRGmF4Wx8mjAUB6sYui3"))

            val gson = GsonBuilder()
                .setLenient()
                .create()
            client.readTimeout(1, TimeUnit.MINUTES)
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}