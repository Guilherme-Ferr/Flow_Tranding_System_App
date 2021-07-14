package com.example.flowtrandingsystem.gui.ui
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.flowtrandingsystem.gui.adapter.ItensInventoryAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.api.ProductCalls
import com.example.flowtrandingsystem.gui.model.ProductAdapter
import kotlinx.android.synthetic.main.inventory.*
import retrofit2.Call
import retrofit2.Response

class InventoryActivity : AppCompatActivity() {

    lateinit var rvItens: RecyclerView
    lateinit var adapterItensEstoque: ItensInventoryAdapter
    lateinit var editSrc: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Produtos Desta Categoria")

        rvItens = findViewById(R.id.recycler_view_inventory_list)
        rvItens.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterItensEstoque = ItensInventoryAdapter(this)

        editSrc = findViewById(R.id.edit_search_product)

        rvItens.adapter = adapterItensEstoque

        loadListItens()

        btn_search_product.setOnClickListener {
            if (editSrc.text.isNotEmpty()){
                searchItens()
            }else{
                loadListItens()
            }
        }
    }
    private  fun loadListItens() {
        val prefs: SharedPreferences = this@InventoryActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedCompanyId = prefs.getInt("COMPANYID", 0)
        val retrivedCategoryName: String = prefs.getString("TYPE", "Nada foi recebido").toString()

        var listaItens: List<ProductAdapter>
        val retrofit = RetrofitApi.getRetrofit()
        val produtosCall = retrofit.create(ProductCalls::class.java)
        val call = produtosCall.getProductByCategorie(retrivedCompanyId, retrivedCategoryName, "Bearer ${retrivedToken}"
        )
        call.enqueue(object : retrofit2.Callback<List<ProductAdapter>>{
            override fun onFailure(call: Call<List<ProductAdapter>>, t: Throwable) {
                Toast.makeText(this@InventoryActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<List<ProductAdapter>>, response: Response<List<ProductAdapter>>) {
                listaItens = (response.body())!!

                adapterItensEstoque.updateListaProdutos(listaItens)
            }
        })
    }
    private fun searchItens() {
        val prefs: SharedPreferences = this@InventoryActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedCompanyId = prefs.getInt("COMPANYID", 0)
        val retrivedCategoryName: String = intent.getStringExtra("productType").toString()

        var listaItens: List<ProductAdapter>
        val retrofit = RetrofitApi.getRetrofit()
        val produtosCall = retrofit.create(ProductCalls::class.java)
        val call = produtosCall.getProductBySearch(retrivedCompanyId, editSrc.text.toString(), retrivedCategoryName,"Bearer ${retrivedToken}")

        call.enqueue(object : retrofit2.Callback<List<ProductAdapter>>{

            override fun onFailure(call: Call<List<ProductAdapter>>, t: Throwable) {
                Toast.makeText(this@InventoryActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<List<ProductAdapter>>, response: Response<List<ProductAdapter>>) {
                listaItens = (response.body())!!

                adapterItensEstoque.updateListaProdutos(listaItens)
            }
        })
    }
}