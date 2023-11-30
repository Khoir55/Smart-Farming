package com.ei.smart_farming.ui.main.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ei.smart_farming.R
import com.ei.smart_farming.utils.PrefManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Pengaturan"

        val pref = PrefManager(this)

        val btnAutoPlot1 = findViewById<SwitchMaterial>(R.id.auto_plot1)
        val btnAutoPlot2 = findViewById<SwitchMaterial>(R.id.auto_plot2)
        val btnAutoLamp = findViewById<SwitchMaterial>(R.id.auto_lamp)

        val dbAutoPlot1 = database.child("control").child("auto-plot1")
        val dbAutoPlot2 = database.child("control").child("auto-plot2")
        val dbAutoLamp = database.child("control").child("auto-lamp")

        btnAutoPlot1.isChecked = pref.stateAutoP1!!
        btnAutoPlot2.isChecked = pref.stateAutoP2!!
        btnAutoLamp.isChecked = pref.stateAutoLamp!!

        btnAutoPlot1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbAutoPlot1.setValue(true)
            } else {
                dbAutoPlot1.setValue(false)
            }
        }

        btnAutoPlot2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbAutoPlot2.setValue(true)
            } else {
                dbAutoPlot2.setValue(false)
            }
        }

        btnAutoLamp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbAutoLamp.setValue(true)
            } else {
                dbAutoLamp.setValue(false)
            }
        }
    }
}