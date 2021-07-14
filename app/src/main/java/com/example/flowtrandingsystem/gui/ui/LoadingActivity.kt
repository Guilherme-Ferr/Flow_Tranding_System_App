package com.example.flowtrandingsystem.gui.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
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

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@LoadingActivity, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}
