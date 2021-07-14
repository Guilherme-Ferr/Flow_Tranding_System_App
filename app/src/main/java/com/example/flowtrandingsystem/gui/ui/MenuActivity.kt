package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.flowtrandingsystem.R
import kotlinx.android.synthetic.main.fragment_initial_menu.*


class MenuActivity : AppCompatActivity(){
    private lateinit var inventoryOption: LinearLayout
    private lateinit var pdvOption: LinearLayout
    private lateinit var reportOption: LinearLayout
//    private lateinit var loadOption: LinearLayout
    private lateinit var drawerLayout: DrawerLayout

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_initial_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Menu Principal")

        inventoryOption = findViewById(R.id.option_inventory)
        pdvOption = findViewById(R.id.option_sell)
        reportOption = findViewById(R.id.option_report)
//        loadOption = findViewById(R.id.loading)
        drawerLayout = findViewById(R.id.drawerLayout)

        inventoryOption.setOnClickListener {
            val intentInventory = Intent(this, ProductTypeActivity::class.java)
            startActivity(intentInventory)
        }
        pdvOption.setOnClickListener{

            val prefs: SharedPreferences = this@MenuActivity.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
            val retrivedUser = prefs.getString("USER", "Nada foi recebido")

//            Toast.makeText(this, "permissao:${retrivedUser}", Toast.LENGTH_SHORT).show()

            val intentSell = Intent(this, PdvActivity::class.java)
                startActivity(intentSell)
        }
        reportOption.setOnClickListener{
            val intentReportCompany = Intent(this, ReportActivity::class.java)
            startActivity(intentReportCompany)
        }
//        loadOption.setOnClickListener{
//            val intentReportCompany = Intent(this, LoadingActivity::class.java)
//            startActivity(intentReportCompany)
//        }
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        main_navigation_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_option_home -> Toast.makeText(this, "Tela Atual", Toast.LENGTH_SHORT).show()
                R.id.nav_option_profile -> goToInfoUser()
                R.id.nav_option_company -> goToInfoCompany()
                R.id.nav_option_logout -> goToLogin()
            }
            true
        }
    }

    private fun goToInfoCompany(){
        val companyScreen = Intent(this, CompanyInfoActivity::class.java)
        startActivity(companyScreen)
    }
    private fun goToLogin(){
        val loginScreen = Intent(this, MainActivity::class.java)
        startActivity(loginScreen)
        finish()
    }
    private fun goToInfoUser(){
        val userScreen = Intent(this, UserInfoActivity::class.java)
        startActivity(userScreen)
    }
    override fun onBackPressed() {
        val layout = findViewById<View>(R.id.drawerLayout) as DrawerLayout
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        // Back?
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Back
            moveTaskToBack(true)
            true
        } else {
            // Return
            super.onKeyDown(keyCode, event)
        }
    }
}