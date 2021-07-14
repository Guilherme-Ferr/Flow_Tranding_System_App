package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.flowtrandingsystem.R
import com.example.flowtrandingsystem.gui.api.CompanyCalls
import com.example.flowtrandingsystem.gui.api.RetrofitApi
import com.example.flowtrandingsystem.gui.model.Company
import kotlinx.android.synthetic.main.company_info.*
import retrofit2.Call
import retrofit2.Response

class CompanyInfoActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var companyName : TextView
    private lateinit var companyReason : TextView
    private lateinit var companyEmail : TextView
    private lateinit var companyPlan : TextView
    private lateinit var companyBusiness : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Informações da Sua Empresa")

        toggle = ActionBarDrawerToggle(this, drawerLayoutCompanyInfo, R.string.open, R.string.close)
        drawerLayoutCompanyInfo.addDrawerListener(toggle)
        toggle.syncState()


        companyName = findViewById(R.id.company_name)
        companyReason = findViewById(R.id.reason_of_company)
        companyEmail = findViewById(R.id.email_of_company)
        companyPlan = findViewById(R.id.plan_of_company)
        companyBusiness = findViewById(R.id.busines_of_company)

        company_navigation_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_option_home -> goToMenu()
                R.id.nav_option_profile -> goToInfoUser()
                R.id.nav_option_company -> Toast.makeText(this, "Tela Atual", Toast.LENGTH_SHORT).show()
                R.id.nav_option_logout -> goToLogin()
            }
            true
        }
        loadInfo()
    }
    private fun goToMenu(){
        val menuScreen = Intent(this, MenuActivity::class.java)
        startActivity(menuScreen)
    }
    private fun goToInfoUser(){
        val userScreen = Intent(this, UserInfoActivity::class.java)
        startActivity(userScreen)
    }
    private fun goToLogin(){
        val loginScreen = Intent(this, MainActivity::class.java)
        startActivity(loginScreen)
    }
    private  fun loadInfo() {
        val prefs: SharedPreferences = this@CompanyInfoActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val retrivedToken = prefs.getString("TOKEN", "Nada foi recebido")
        val retrivedCompanyId = prefs.getInt("COMPANYID", 0)

        var companyInfo: Company
        val retrofit = RetrofitApi.getRetrofit()
        val companyCall = retrofit.create(CompanyCalls::class.java)
        val call = companyCall.getInfoFromCompany(retrivedCompanyId, "Bearer ${retrivedToken}")

        call.enqueue(object : retrofit2.Callback<Company>{

            override fun onFailure(call: Call<Company>, t: Throwable) {
                Toast.makeText(this@CompanyInfoActivity, "Ops! Acho que ocorreu um problema.", Toast.LENGTH_SHORT).show()
                Log.e("ERRO_CONEXÃO", t.message.toString())
            }
            override fun onResponse(call: Call<Company>, response: Response<Company>) {
                companyInfo = response.body()!!

                companyName.text = companyInfo.fantasy_name
                companyReason.text = companyInfo.social_reason
                companyEmail.text = companyInfo.commercial_email
                companyPlan.text = companyInfo.Plan.plan_name
                companyBusiness.text = companyInfo.nature_of_the_business
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val layout = findViewById<View>(R.id.drawerLayoutCompanyInfo) as DrawerLayout
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
