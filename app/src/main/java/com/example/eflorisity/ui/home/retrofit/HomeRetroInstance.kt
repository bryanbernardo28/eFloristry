package com.example.eflorisity.login.retrofit

import com.example.eflorisity.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeRetroInstance {

    companion object{
        val URL = Config()
        val BASE_URL = "http://${URL.baseUrl}/api/mobile/"

        fun getInstance(): Retrofit{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}