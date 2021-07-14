package com.example.flowtrandingsystem.gui.api

import com.example.flowtrandingsystem.gui.model.Costumer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CostumerCalls {
    @POST("/costumer")
    fun postCostumer(@Body costumer: Costumer, @Header("Authorization") token: String?) : Call<Costumer>
}