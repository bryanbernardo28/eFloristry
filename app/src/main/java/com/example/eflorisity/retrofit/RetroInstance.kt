package com.example.eflorisity.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder


class RetroInstance {
    companion object{
//        val baseUrl = "http://10.0.2.2:8000"
//        val baseUrl = "http://192.168.50.69:8000"
        val baseUrl = "https://www.eflouristry.com"
        val BASE_URL = "$baseUrl/api/mobile/"

        fun getInstance(): Retrofit {
            val client = OkHttpClient.Builder()
            val gson = GsonBuilder()
                .setLenient()
                .create()
            client.readTimeout(1, TimeUnit.MINUTES)
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}