package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat.format
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.api.ProductCalls
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.model.Logbook
import com.example.flowtrandingsystem.gui.model.ProductAdapter
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_initial_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String.format
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DatasheetActivity : AppCompatActivity() {

    private lateinit var productName: TextView
    private lateinit var productType: TextView
    private lateinit var unitPrice: TextView
    private lateinit var productQtd: TextView
    private lateinit var barCode: TextView
    private lateinit var acquisitionDate: TextView
    private lateinit var expiratonDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.datasheet)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Informações do Produto")

        productName = findViewById(R.id.product_name_datasheet)
        productType = findViewById(R.id.type_of_product_datasheet)
        unitPrice = findViewById(R.id.value_un_price_datasheet)
        productQtd = findViewById(R.id.amount_of_inventory_datasheet)
        barCode = findViewById(R.id.product_barcode_datasheet)
        acquisitionDate = findViewById(R.id.acq_date_datsheet)
        expiratonDate = findViewById(R.id.expiration_date_datasheet)

        loadInfo()
    }

    private fun loadInfo() {
        val prefs: SharedPreferences = this@DatasheetActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedLogId: Int = intent.getIntExtra("logId", 0)

        var productInfo: Logbook
        val retrofit = RetrofitApi.getRetrofit()
        val productCall = retrofit.create(ProductCalls::class.java)
        val call = productCall.getLogProductById(retrivedLogId, "Bearer ${retrivedToken}")

        call.enqueue(object : Callback<Logbook> {

            override fun onFailure(call: Call<Logbook>, t: Throwable) {
                Toast.makeText(this@DatasheetActivity, "Estoque Não Cadastrado Ainda.", Toast.LENGTH_LONG).show()
                Log.e("ERRO_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<Logbook>, response: Response<Logbook>) {
                productInfo = response.body()!!

                if (productInfo.Lot.expiration_date.isNullOrBlank()){

                    val formatedacquisitionDate = productInfo.date_of_acquisition.split("-")
                    acquisitionDate.text = "${formatedacquisitionDate[2]}-${formatedacquisitionDate[1]}-${formatedacquisitionDate[0]}"
                    expiratonDate.text = "N/A"

                    productName.text = productInfo.Product.product_name
                    productType.text = productInfo.Product.ProductType.type
                    unitPrice.text = "R$ ${String.format("%.2f", productInfo.Product.cost_per_item)}"
                    productQtd.text = productInfo.quantity_acquired.toString()
                    barCode.text = productInfo.Product.bar_code

                }else{

                    val formatedacquisitionDate = productInfo.date_of_acquisition.split("-")
                    val formatedexpiratonDate = productInfo.Lot.expiration_date.split("-")
                    expiratonDate.text = "${formatedexpiratonDate[2]}-${formatedexpiratonDate[1]}-${formatedexpiratonDate[0]}"
                    acquisitionDate.text = "${formatedacquisitionDate[2]}-${formatedacquisitionDate[1]}-${formatedacquisitionDate[0]}"

                    productName.text = productInfo.Product.product_name
                    productType.text = productInfo.Product.ProductType.type
                    unitPrice.text = "R$ ${String.format("%.2f", productInfo.Product.cost_per_item)}"
                    productQtd.text = productInfo.quantity_acquired.toString()
                    barCode.text = productInfo.Product.bar_code

                    val prefs: SharedPreferences = this@DatasheetActivity.getSharedPreferences(
                        "type",
                        Context.MODE_PRIVATE
                    )

                    prefs.edit().putString("typeProduct", productInfo.Product.ProductType.type).apply()
                }
            }
        })
    }
//
//    override fun onBackPressed() {
//
//        val categoryIntent = Intent(this@DatasheetActivity, InventoryActivity::class.java)
//        categoryIntent.putExtra("productType", productType.toString())
//        startActivity(categoryIntent)
//    }
}



