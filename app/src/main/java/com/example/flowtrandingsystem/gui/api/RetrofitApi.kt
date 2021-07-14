package com.example.flowtrandingsystem.gui.api

import com.example.flowtrandingsystem.gui.api.UrlApi.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {
    companion object {
        fun getRetrofit() : Retrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit
        }
    }
}
