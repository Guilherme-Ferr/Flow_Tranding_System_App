package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.adapter.BarCodeAdapter
import com.example.flowtrandingsystem.gui.api.CostumerCalls
import com.example.flowtrandingsystem.gui.api.ProductCalls
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.api.SaleAndPurchaseCalls
import com.example.flowtrandingsystem.gui.model.*
import kotlinx.android.synthetic.main.add_payment_method_pdv.view.*
import kotlinx.android.synthetic.main.client_register_pdv.view.*
import kotlinx.android.synthetic.main.pdv.*
import retrofit2.Call
import retrofit2.Response
import java.io.Serializable


open class PdvActivity : AppCompatActivity(), Serializable{

    lateinit var rvItens: RecyclerView
    lateinit var adapterItensList: BarCodeAdapter
    lateinit var editCpfClient: EditText
    lateinit var editQtdDiscount: EditText

    lateinit var subTotal: TextView

    lateinit var optionMethods: Spinner

    var listProducts: ArrayList<ProductAdapter> = ArrayList<ProductAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdv)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "PDV"

        rvItens = findViewById(R.id.recycler_view_product_sale)
        subTotal = findViewById(R.id.subTotal_pdv)

        rvItens.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapterItensList = BarCodeAdapter(this)

        rvItens.adapter = adapterItensList

        pdv_client_register.setOnClickListener {
            clientRegister()
        }
        img_camera_code.setOnClickListener {
            val scanScreen = Intent(this, ScannerActivity::class.java)
            startActivity(scanScreen)
        }
        finish_sale.setOnClickListener {
            finishSale()
        }
        add_code.setOnClickListener {
            addProductByCode()
        }

        addProductByCamera()

    }
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        Log.e("ERRO_SAVE","In Saved")
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle, outState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        Log.e("ERRO_SAVE","In Restored")
//
//    }

    fun addProductByCamera() {
        val editCode = findViewById<EditText>(R.id.pdv_activity_product_code)
        val scannedCode: String = intent.getStringExtra("barCode").toString()

        if(scannedCode == "null"){
            Toast.makeText(this, "Insira ou escaneie o código", Toast.LENGTH_LONG).show()
        }else{
            editCode.setText(scannedCode)
//            addProductByCode()
        }
    }
    fun addProductByCode() {
        val prefs: SharedPreferences = this@PdvActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedCompany = prefs.getInt("COMPANYID", 0)

        val editQtde = findViewById<EditText>(R.id.pdv_qtde_sale)
        val editCode = findViewById<EditText>(R.id.pdv_activity_product_code)

        if (editCode.text.isEmpty()){
            Toast.makeText(this, "Insira Um Código Valido", Toast.LENGTH_LONG).show()
        }else{

            if(editQtde.text.toString().isNullOrBlank() || editQtde.text.toString().toInt() === 0) {
                Toast.makeText(this@PdvActivity, "Quantidade Invalida", Toast.LENGTH_LONG).show()

            }else{

                var itemProduct: ProductAdapter
                val retrofit = RetrofitApi.getRetrofit()
                val productBarCode = retrofit.create(ProductCalls::class.java)
                val call = productBarCode.getBarProduct(editCode.text.toString(), retrivedCompany,"Bearer ${retrivedToken}")

                call.enqueue(object : retrofit2.Callback<ProductAdapter>{

                    override fun onFailure(call: Call<ProductAdapter>, t: Throwable) {
                        Toast.makeText(this@PdvActivity, "Verifique o Codigo Novamente", Toast.LENGTH_LONG).show()
                        Log.e("ERRO_CONEXÃO", t.message.toString())
                    }
                    override fun onResponse(call: Call<ProductAdapter>, response: Response<ProductAdapter>) {
                        itemProduct = response.body()!!

                        var quantity = 1

                        quantity = editQtde.text.toString().toInt()

                        val itemTotalValue = quantity * itemProduct.cost_per_item

                        val prefs: SharedPreferences = this@PdvActivity.getSharedPreferences(
                            "total",
                            Context.MODE_PRIVATE
                        )

                        prefs.edit().putFloat("total", itemTotalValue.toFloat()).apply()
                        prefs.edit().putString("itenQtd", itemProduct.qtd.toString()).apply()
                        prefs.edit().putString("itenId", itemProduct.id.toString()).apply()

                        itemProduct.qtd = quantity

                        listProducts.add(itemProduct)

                        val codeIntent = Intent(this@PdvActivity, BarCodeAdapter::class.java)
                        codeIntent.putExtra("qtd", quantity)

                        adapterItensList.updateListProducts(listProducts.toList())

                        val list = arrayListOf<Double>()

                        for (item in listProducts) {
                            list.add((item.cost_per_item * item.qtd))
                        }

                        val cost_total =
                            list.reduce { totalValue, currentItem -> totalValue + currentItem }.toDouble()

                        if (listProducts.isEmpty()) {
                            subTotal.text = 0.0.toString()
                        } else {
                            subTotal.text = "$${String.format("%.2f", cost_total)}"

                            remove_code.setOnClickListener {

                                if (listProducts.size > 0) {
                                    val index = listProducts.size - 1

                                    listProducts.removeAt(index)

                                    adapterItensList.notifyItemRemoved(index)

                                    adapterItensList.updateListProducts(listProducts)

                                    subTotal.text = "$${String.format("%.2f", cost_total.minus(itemTotalValue))}"
                                }
                            }
                        }
                    }
                })
            }
        }
    }
    fun finishSale(){
        val prefs: SharedPreferences = this@PdvActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedBranchId = prefs.getInt("BRANCHID", 0)
        val retrivedItenId = prefs.getInt("itenId", 0)
        val retrivedItenQtd = prefs.getInt("itenQtd", 1)


        if (listProducts.isEmpty()){
            Toast.makeText(this, "Lista de Compras Vazia", Toast.LENGTH_SHORT).show()
        }else {

            val alerDialog = LayoutInflater.from(this).inflate(R.layout.add_payment_method_pdv, null)
            val dialogBuilder = AlertDialog.Builder(this).setView(alerDialog)
            val alertShow = dialogBuilder.show()

            alertShow.setCanceledOnTouchOutside(false)

            optionMethods = alerDialog.findViewById(R.id.spinner_methods) as Spinner

            val options = arrayOf("Debito", "Credito", "Boleto", "PicPay")

            optionMethods.adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

            optionMethods.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(this@PdvActivity, "Selecione uma opção", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }
            }
            alerDialog.button_cancel_sale.setOnClickListener {
                alertShow.dismiss()
            }
            alerDialog.button_finish.setOnClickListener {

                editQtdDiscount = alerDialog.findViewById(R.id.edit_add_discount_pdv)

                var sale = Sale()

                sale.payment_method_id = 1
                sale.branch_id = retrivedBranchId

                sale.discount = editQtdDiscount.text.toString().toInt()

                val listOfItens = ArrayList<Itens>()

                listProducts.forEach {

                    var index = 0

                    val iten: Itens = Itens(product_id = it.id, quantity = it.qtd)

                    if (listOfItens.size > 0) {
                        index = listOfItens.size - 1
                    }

                    listOfItens.add(iten)
                }

                sale.items = listOfItens

                val retrofit = RetrofitApi.getRetrofit()
                val saleCall = retrofit.create(SaleAndPurchaseCalls::class.java)
                val call = saleCall.postSale(sale, "Bearer ${retrivedToken}")

                call.enqueue(object : retrofit2.Callback<Sale> {
                    override fun onFailure(call: Call<Sale>, t: Throwable) {
                        Toast.makeText(
                            this@PdvActivity,
                            "Ops! Acho que ocorreu um problema.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("ERRO_CONEXÃO", t.message.toString())
                    }

                    override fun onResponse(call: Call<Sale>, response: Response<Sale>) {
                        sale = response.body()!!

                    }
                })
                alertShow.dismiss()
                startActivity(getIntent())
                finish()
                Toast.makeText(this, "Venda Concluida", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun clientRegister() {
        val prefs: SharedPreferences = this@PdvActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")

        val alerDialog = LayoutInflater.from(this).inflate(R.layout.client_register_pdv, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(alerDialog)
        val alertShow = dialogBuilder.show()

        alertShow.setCanceledOnTouchOutside(false)

        alerDialog.button_cancel_client_register.setOnClickListener {
            alertShow.dismiss()
        }
        alerDialog.button_save_client_register.setOnClickListener {

            editCpfClient = alerDialog.findViewById(R.id.edit_client_register_cpf)

            if (editCpfClient.text.isEmpty()){
                Toast.makeText(this, "Insira Um CPF Valido", Toast.LENGTH_SHORT).show()
            }else{

                var costumer = Costumer()
                costumer.cpf = editCpfClient.text.toString().replace(".", "").replace("-", "")
                val retrofit = RetrofitApi.getRetrofit()
                val costumerCall = retrofit.create(CostumerCalls::class.java)
                val call = costumerCall.postCostumer(costumer, token = "Bearer ${retrivedToken}")

                call.enqueue(object : retrofit2.Callback<Costumer>{

                    override fun onFailure(call: Call<Costumer>, t: Throwable) {
                        Toast.makeText(this@PdvActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                        Log.e("ERRO_CONEXÃO", t.message.toString())
                    }
                    override fun onResponse(call: Call<Costumer>, response: Response<Costumer>) {
                        costumer = response.body()!!

                        Toast.makeText(this@PdvActivity, "Cliente do CPF: ${costumer.cpf} Cadstrado!", Toast.LENGTH_SHORT).show()

                        alertShow.dismiss()
                    }

                })
            }
        }
    }
    override fun onBackPressed() {
        Toast.makeText(this, "Utilize o Botão do Menu Para Voltar", Toast.LENGTH_SHORT).show()
    }
}
