package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.adapter.ReportSaleAdapter
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.api.SaleAndPurchaseCalls
import com.example.flowtrandingsystem.gui.model.Purchase
import com.example.flowtrandingsystem.gui.model.Sale
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import kotlinx.android.synthetic.main.report.*
import retrofit2.Call
import retrofit2.Response

class ReportActivity : AppCompatActivity() {

    lateinit var adapterSales: ReportSaleAdapter
    lateinit var tvTotalSale: TextView
    lateinit var tvTotalPurchase: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Relatorios")

        tvTotalSale = findViewById(R.id.total_sales)
        tvTotalPurchase = findViewById(R.id.total_purchases)

        adapterSales = ReportSaleAdapter(this)

        btn_refresh.setOnClickListener {
            startActivity(getIntent())
            finish()
        }

        loadTotalSale()
        loadTotalPurchase()
        setPieChartData()
    }


    private fun loadTotalSale() {
        val prefs: SharedPreferences = this@ReportActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")

        var totalSale: List<Sale>
        val retrofit = RetrofitApi.getRetrofit()
        val reportCall = retrofit.create(SaleAndPurchaseCalls::class.java)
        val call = reportCall.getTotalSales("Bearer ${retrivedToken}")

        call.enqueue(object : retrofit2.Callback<List<Sale>> {

            override fun onFailure(call: Call<List<Sale>>, t: Throwable) {
                Toast.makeText(
                    this@ReportActivity,
                    "Ops! Acho que ocorreu um problema.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }

            override fun onResponse(call: Call<List<Sale>>, response: Response<List<Sale>>) {
                totalSale = response.body()!!

                tvTotalSale.text = totalSale.size.toString()

                val value = totalSale.size

                val prefs: SharedPreferences = this@ReportActivity.getSharedPreferences(
                    "preferencias",
                    Context.MODE_PRIVATE
                )

                //Tentar passar a permissao usuario inteiro
                prefs.edit().putFloat("SALEVALUE", value.toFloat()).apply()

            }
        })
    }
    private fun loadTotalPurchase() {
        val prefs: SharedPreferences = this@ReportActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")

        var totalPurchase: List<Purchase>
        val retrofit = RetrofitApi.getRetrofit()
        val reportCall = retrofit.create(SaleAndPurchaseCalls::class.java)
        val call = reportCall.getTotalPurchases("Bearer ${retrivedToken}")

        call.enqueue(object : retrofit2.Callback<List<Purchase>> {

            override fun onFailure(call: Call<List<Purchase>>, t: Throwable) {
                Toast.makeText(
                    this@ReportActivity,
                    "Ops! Acho que ocorreu um problema.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Erro_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<List<Purchase>>, response: Response<List<Purchase>>) {
                totalPurchase = response.body()!!

                tvTotalPurchase.text = totalPurchase.size.toString()

                val value = totalPurchase.size

                val prefs: SharedPreferences = this@ReportActivity.getSharedPreferences(
                    "preferencias",
                    Context.MODE_PRIVATE
                )

                //Tentar passar a permissao usuario inteiro
                prefs.edit().putFloat("PURCHASEVALUE", value.toFloat()).apply()

            }
        })
    }
    private fun setPieChartData(){
        val prefs: SharedPreferences = this@ReportActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedSale = prefs.getFloat("SALEVALUE", 0.0F)
        val retrivedPurchase = prefs.getFloat("PURCHASEVALUE", 0.0F)

        val xvalues = ArrayList<String>()
        xvalues.add("DESPESAS")
        xvalues.add("LUCROS")

        val yvalues = ArrayList<Int>()
        yvalues.add(retrivedPurchase.toInt())
        yvalues.add(retrivedSale.toInt())

        val pieChartEntry = ArrayList<Entry>()
        for ((i, item) in yvalues.withIndex())
        {
            pieChartEntry.add(Entry(item.toFloat(), i))
        }

        //colors
        val colorsChart = ArrayList<Int>()
        colorsChart.add(resources.getColor(R.color.primaryDark))
        colorsChart.add(resources.getColor(R.color.primary))

        //fill the chart
        val piedataSet = PieDataSet(pieChartEntry, "")

        piedataSet.colors = colorsChart

        piedataSet.sliceSpace = 2f

        val chartText = PieData(xvalues, piedataSet)
        chartText.setValueTextSize(20f)

        val data = PieData(xvalues, piedataSet)
        pieChart.data = data

        pieChart.setDescription("Balanço de compras e vendas")
        pieChart.setDescriptionPosition(0f, 500f)
        pieChart.setDescriptionTextSize(12f)
        pieChart.animateY(2000)

        /*val legend: Legend = pieChart.legend
        legend.position = Legend.LegendPosition.LEFT_OF_CHART*/

    }
}