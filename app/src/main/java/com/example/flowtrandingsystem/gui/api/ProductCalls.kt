package com.example.flowtrandingsystem.gui.api


import com.example.flowtrandingsystem.gui.model.Logbook
import com.example.flowtrandingsystem.gui.model.ProductAdapter
import com.example.flowtrandingsystem.gui.model.ProductType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductCalls {
    @GET("/logbook/find/{id}")
    fun getLogProductById(@Path("id") id: Int, @Header("Authorization") token: String?): Call<Logbook>

    @GET("/company/{companyId}/product/barCode/{barCode}")
    fun getBarProduct(@Path("barCode") barCode: String, @Path("companyId") companyId: Int, @Header("Authorization") token: String?): Call<ProductAdapter>

    @GET("/productType")
    fun getProductType(@Header("Authorization") token: String?): Call<List<ProductType>>

    @GET("/company/{companyId}/product?")
    fun getProductByCategorie(@Path("companyId") companyId: Int, @Query("product_type") product_type: String, @Header("Authorization") token: String?): Call<List<ProductAdapter>>

    @GET("/company/{companyId}/product?")
    fun getProductBySearch(
        @Path("companyId") companyId: Int, @Query("product_name") product_name: String, @Query("product_type") product_type: String, @Header("Authorization") token: String?): Call<List<ProductAdapter>>

}


