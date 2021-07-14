package com.example.flowtrandingsystem.gui.api

import com.example.flowtrandingsystem.gui.model.Permissions
import com.example.flowtrandingsystem.gui.model.Token
import com.example.flowtrandingsystem.gui.model.User
import com.example.flowtrandingsystem.gui.model.UserLogin
import retrofit2.Call
import retrofit2.http.*


interface UserCalls {

    @GET("/user/find/{id}")
    fun getInfoFromUser(@Path("id") id: Int, @Header("Authorization") token: String?): Call<User>

    @POST("/session")
    fun postLogin(@Body user: UserLogin) : Call<Token>

}