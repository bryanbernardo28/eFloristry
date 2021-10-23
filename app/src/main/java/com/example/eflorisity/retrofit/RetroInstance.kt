package com.example.eflorisity.retrofit

import com.example.eflorisity.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

import com.google.gson.Gson




class RetroInstance {
    companion object{
        val URL = Config()
        val BASE_URL = "http://${URL.baseUrl}/api/mobile/"

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