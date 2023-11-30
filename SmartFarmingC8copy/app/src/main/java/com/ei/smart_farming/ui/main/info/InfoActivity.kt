package com.ei.smart_farming.ui.main.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.ei.smart_farming.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        supportActionBar?.title = "Informasi"

        val backDashboard = findViewById<Button>(R.id.btn_back_dashboard)

        backDashboard.setOnClickListener {
            onBackPressed()
        }
    }
}