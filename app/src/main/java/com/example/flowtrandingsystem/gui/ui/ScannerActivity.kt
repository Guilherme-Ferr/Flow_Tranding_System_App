package com.example.flowtrandingsystem.gui.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.flowtrandingsystem.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

class ScannerActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 101

    private lateinit var codeScanner: CodeScanner
    private lateinit var scanner_view: CodeScannerView
    private lateinit var tvTextView: TextView
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_scanner)

        scanner_view = findViewById(R.id.scanner_view)
        tvTextView = findViewById(R.id.tv_textView)
        tvResultado = findViewById(R.id.tv_resultado)

        setupPermission()

        codeScanner()
    }

    private fun codeScanner() {

        codeScanner = CodeScanner(this, scanner_view)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    tvResultado.text = it.text

                    val codeIntent = Intent(this@ScannerActivity, PdvActivity::class.java)
                    codeIntent.putExtra("barCode", it.text)
                    startActivity(codeIntent)
                    finish()
                }
            }
            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("Main", "Erro de Inicialização da Camera: ${it.message}")
                }
            }
        }
        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Precisa da permissao da camera para usar o App", Toast.LENGTH_SHORT).show()
                } else {
                    //sucexo
                }
            }
        }
    }
}
