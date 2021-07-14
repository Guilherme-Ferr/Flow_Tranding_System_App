package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.adapter.ProductsCategoriesAdapter
import com.example.flowtrandingsystem.gui.api.ProductCalls
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.model.Logbook
import com.example.flowtrandingsystem.gui.model.ProductType
import retrofit2.Call
import retrofit2.Response

class ProductTypeActivity : AppCompatActivity() {

    lateinit var rvProductCategories: RecyclerView
    lateinit var adapterProductsCategories: ProductsCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_type)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Categorias de Produtos")

        rvProductCategories = findViewById(R.id.recycler_view_category_list)
        rvProductCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterProductsCategories = ProductsCategoriesAdapter(this)

        rvProductCategories.adapter = adapterProductsCategories

        loadCategoriesList()
    }

    private fun loadCategoriesList() {
        val prefs: SharedPreferences = this@ProductTypeActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")

        var categoryList: List<ProductType>
        val retrofit = RetrofitApi.getRetrofit()
        val categoryCall = retrofit.create(ProductCalls::class.java)
        val call = categoryCall.getProductType("Bearer ${retrivedToken}")

        call.enqueue(object : retrofit2.Callback<List<ProductType>>{

            override fun onFailure(call: Call<List<ProductType>>, t: Throwable) {
                Toast.makeText(this@ProductTypeActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<List<ProductType>>, response: Response<List<ProductType>>) {
                categoryList = response.body()!!

                adapterProductsCategories.updateCategoryList(categoryList)


            }
        })
    }
}