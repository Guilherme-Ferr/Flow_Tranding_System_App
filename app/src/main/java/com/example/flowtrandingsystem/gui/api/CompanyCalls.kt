package com.example.flowtrandingsystem.gui.api

import com.example.flowtrandingsystem.gui.model.Company
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CompanyCalls {

    @GET("/company/find/{id}")
    fun getInfoFromCompany(@Path("id") id: Int, @Header("Authorization") Token: String?): Call<Company>

}