package com.ei.smart_farming.ui.main.dashboard

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.ei.smart_farming.R
import com.ei.smart_farming.ui.main.data.DataActivity
import com.ei.smart_farming.ui.main.info.InfoActivity
import com.ei.smart_farming.ui.main.settings.SettingsActivity
import com.ei.smart_farming.utils.PrefManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.marcoscg.dialogsheet.DialogSheet
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference

    var dataTime1P1 = String.toString()
    var dataTime2P1 = String.toString()

    var dataTime1P2 = String.toString()
    var dataTime2P2 = String.toString()

    var dataDuration1P1 = String.toString()
    var dataDuration2P1 = String.toString()

    var dataDuration1P2 = String.toString()
    var dataDuration2P2 = String.toString()

    var dataUnit1P1 = String.toString()
    var dataUnit2P1 = String.toString()

    var dataUnit1P2 = String.toString()
    var dataUnit2P2 = String.toString()

    var dataTimeOnLamp = String.toString()
    var dataTimeOffLamp = String.toString()

    var dataDay1P1 = false
    var dataDay2P1 = false
    var dataDay3P1 = false
    var dataDay4P1 = false
    var dataDay5P1 = false
    var dataDay6P1 = false
    var dataDay7P1 = false

    var dataDay1P2 = false
    var dataDay2P2 = false
    var dataDay3P2 = false
    var dataDay4P2 = false
    var dataDay5P2 = false
    var dataDay6P2 = false
    var dataDay7P2 = false

    var dataDay1Lamp = false
    var dataDay2Lamp = false
    var dataDay3Lamp = false
    var dataDay4Lamp = false
    var dataDay5Lamp = false
    var dataDay6Lamp = false
    var dataDay7Lamp = false

    var graphState = false

    val tag = "DashboardActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.elevation = 0f

        val btnPlot1 = findViewById<ToggleButton>(R.id.btn_plot1)
        val btnPlot2 = findViewById<ToggleButton>(R.id.btn_plot2)
        val btnLamp = findViewById<ToggleButton>(R.id.btn_lamp)
        val btnDetail = findViewById<Button>(R.id.btn_detail)

        val dbPlot1 = database.child("control").child("plot1")
        val dbPlot2 = database.child("control").child("plot2")
        val dbLamp = database.child("control").child("lamp")

        val shimmerControlP1 = findViewById<ShimmerFrameLayout>(R.id.shimmer_control_p1)
        val shimmerControlP2 = findViewById<ShimmerFrameLayout>(R.id.shimmer_control_p2)
        val shimmerControlLamp = findViewById<ShimmerFrameLayout>(R.id.shimmer_control_lamp)


        btnPlot1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbPlot1.setValue(true)
            } else {
                dbPlot1.setValue(false)
            }
        }

        btnPlot2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbPlot2.setValue(true)
            } else {
                dbPlot2.setValue(false)
            }
        }

        btnLamp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbLamp.setValue(true)
            } else {
                dbLamp.setValue(false)
            }
        }

        btnDetail.setOnClickListener {
            val i = Intent(applicationContext, DataActivity::class.java)
            startActivity(i)
        }

        shimmerControlP1.visibility = View.VISIBLE
        shimmerControlP2.visibility = View.VISIBLE
        shimmerControlLamp.visibility = View.VISIBLE
        btnPlot1.visibility = View.GONE
        btnPlot2.visibility = View.GONE
        btnLamp.visibility = View.GONE

        dbPlot1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value as Boolean
                if(dataSnapshot.exists()){
                    btnPlot1.isChecked = data
                    shimmerControlP1.visibility = View.GONE
                    btnPlot1.visibility = View.VISIBLE
                } else {
                    shimmerControlP1.visibility = View.VISIBLE
                    btnPlot1.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        dbPlot2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value as Boolean
                if(dataSnapshot.exists()){
                    btnPlot2.isChecked = data
                    shimmerControlP2.visibility = View.GONE
                    btnPlot2.visibility = View.VISIBLE
                } else {
                    shimmerControlP2.visibility = View.VISIBLE
                    btnPlot2.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        dbLamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value as Boolean
                if(dataSnapshot.exists()){
                    btnLamp.isChecked = data
                    shimmerControlLamp.visibility = View.GONE
                    btnLamp.visibility = View.VISIBLE
                } else {
                    shimmerControlLamp.visibility = View.VISIBLE
                    btnLamp.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        getRTData()
        getPref()
        chart()
        settingsPlot1()
        settingsPlot2()
        settingsLamp()
    }

    private fun getRTData(){
        val shimmerRealtimeData = findViewById<ShimmerFrameLayout>(R.id.shimmer_realtime_data)
        val realtimeDataBox = findViewById<GridLayout>(R.id.realtime_data_box)

        val tag = "DashboardActivity"
        val tvPlot1 = findViewById<TextView>(R.id.tv_p1)
        val tvPlot2 = findViewById<TextView>(R.id.tv_p2)
        val tvPlot3 = findViewById<TextView>(R.id.tv_p3)
        val tvPlot4 = findViewById<TextView>(R.id.tv_p4)

        val tvCondition1 = findViewById<TextView>(R.id.tv_condition_p1)
        val tvCondition2 = findViewById<TextView>(R.id.tv_condition_p2)
        val tvCondition3 = findViewById<TextView>(R.id.tv_condition_p3)
        val tvCondition4 = findViewById<TextView>(R.id.tv_condition_p4)

        shimmerRealtimeData.visibility = View.VISIBLE
        realtimeDataBox.visibility = View.GONE

        val dbPlot1 = database.child("monitoring").child("temperature")
        val dbPlot2 = database.child("monitoring").child("humidity")
        val dbPlot3 = database.child("monitoring").child("plot1")
        val dbPlot4 = database.child("monitoring").child("plot2")
        val shimmerTime: Long = 1000

        dbPlot1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                if (data == null){
                    val err = "error"
                    tvPlot1.text = err
                    tvCondition1.text = err
                } else {
                    if (data is String){
                        val err = "error"
                        tvPlot1.text = err
                        tvCondition1.text = err
                    } else {
                        tvPlot1.text = data.toString()
                        if(dataSnapshot.exists()){
                            Handler(Looper.getMainLooper()).postDelayed({
                                shimmerRealtimeData.visibility = View.GONE
                                realtimeDataBox.visibility = View.VISIBLE
                            }, shimmerTime)
                        }else{
                            shimmerRealtimeData.visibility = View.VISIBLE
                            realtimeDataBox.visibility = View.GONE
                        }
                        when (data.toString().toFloat()) {
                            in 0f..25f -> {
                                val sd = "Dingin"
                                tvCondition1.text = sd
                            }
                            in 26f..35f -> {
                                val sn = "Normal"
                                tvCondition1.text = sn
                            }
                            in 36f..100f -> {
                                val sp = "Panas"
                                tvCondition1.text = sp
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        dbPlot2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                if (data == null){
                    val err = "error"
                    tvPlot2.text = err
                    tvCondition1.text = err
                } else {
                    if (data is String){
                        val err = "error"
                        tvPlot2.text = err
                        tvCondition1.text = err
                    } else {
                        tvPlot2.text = data.toString()
                        if(dataSnapshot.exists()){
                            Handler(Looper.getMainLooper()).postDelayed({
                                shimmerRealtimeData.visibility = View.GONE
                                realtimeDataBox.visibility = View.VISIBLE
                            }, shimmerTime)
                        }else{
                            shimmerRealtimeData.visibility = View.VISIBLE
                            realtimeDataBox.visibility = View.GONE
                        }
                        when (data.toString().toFloat()) {
                            in 0f..44f -> {
                                val kk = "Kering"
                                tvCondition2.text = kk
                            }
                            in 45f..70f -> {
                                val kn = "Normal"
                                tvCondition2.text = kn
                            }
                            in 71f..100f -> {
                                val kl = "Lembab"
                                tvCondition2.text = kl
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        dbPlot3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                if (data == null){
                    val err = "error"
                    tvPlot3.text = err
                    tvCondition1.text = err
                } else {
                    if (data is String){
                        val err = "error"
                        tvPlot3.text = err
                        tvCondition1.text = err
                    } else {
                        tvPlot3.text = data.toString()
                        if(dataSnapshot.exists()){
                            Handler(Looper.getMainLooper()).postDelayed({
                                shimmerRealtimeData.visibility = View.GONE
                                realtimeDataBox.visibility = View.VISIBLE
                            }, shimmerTime)
                        }else{
                            shimmerRealtimeData.visibility = View.VISIBLE
                            realtimeDataBox.visibility = View.GONE
                        }
                        when (data.toString().toFloat()) {
                            in 0f..33f -> {
                                val tk = "Tanah Kering"
                                tvCondition3.text = tk
                            }
                            in 34f..66f -> {
                                val tl = "Tanah Lembab"
                                tvCondition3.text = tl
                            }
                            in 67f..100f -> {
                                val tb = "Tanah Basah"
                                tvCondition3.text = tb
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        dbPlot4.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value
                if (data == null){
                    val err = "error"
                    tvPlot4.text = err
                    tvCondition1.text = err
                } else {
                    if (data is String){
                        val err = "error"
                        tvPlot4.text = err
                        tvCondition1.text = err
                    } else {
                        tvPlot4.text = data.toString()
                        if(dataSnapshot.exists()){
                            Handler(Looper.getMainLooper()).postDelayed({
                                shimmerRealtimeData.visibility = View.GONE
                                realtimeDataBox.visibility = View.VISIBLE
                            }, shimmerTime)
                        }else{
                            shimmerRealtimeData.visibility = View.VISIBLE
                            realtimeDataBox.visibility = View.GONE
                        }
                        when (data.toString().toFloat()) {
                            in 0f..33f -> {
                                val tk = "Tanah Kering"
                                tvCondition4.text = tk
                            }
                            in 34f..66f -> {
                                val tl = "Tanah Lembab"
                                tvCondition4.text = tl
                            }
                            in 67f..100f -> {
                                val tb = "Tanah Basah"
                                tvCondition4.text = tb
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
    }

    private fun getPref(){
        val tag = "readPref"

        // Get Bottom Sheet Pref
        val dbTime1P1 = database.child("settings").child("plot1").child("time1")
        val dbTime2P1 = database.child("settings").child("plot1").child("time2")

        val dbTime1P2 = database.child("settings").child("plot2").child("time1")
        val dbTime2P2 = database.child("settings").child("plot2").child("time2")

        val dbDuration1P1 = database.child("settings").child("plot1").child("duration1")
        val dbDuration2P1 = database.child("settings").child("plot1").child("duration2")

        val dbDuration1P2 = database.child("settings").child("plot2").child("duration1")
        val dbDuration2P2 = database.child("settings").child("plot2").child("duration2")

        val dbUnit1P1 = database.child("settings").child("plot1").child("unit1")
        val dbUnit2P1 = database.child("settings").child("plot1").child("unit2")

        val dbUnit1P2 = database.child("settings").child("plot2").child("unit1")
        val dbUnit2P2 = database.child("settings").child("plot2").child("unit2")

        val dbTimeOnLamp = database.child("settings").child("lamp").child("time-on")
        val dbTimeOffLamp = database.child("settings").child("lamp").child("time-off")

        val dbDay1P1 = database.child("settings").child("plot1").child("day1")
        val dbDay2P1 = database.child("settings").child("plot1").child("day2")
        val dbDay3P1 = database.child("settings").child("plot1").child("day3")
        val dbDay4P1 = database.child("settings").child("plot1").child("day4")
        val dbDay5P1 = database.child("settings").child("plot1").child("day5")
        val dbDay6P1 = database.child("settings").child("plot1").child("day6")
        val dbDay7P1 = database.child("settings").child("plot1").child("day7")

        val dbDay1P2 = database.child("settings").child("plot2").child("day1")
        val dbDay2P2 = database.child("settings").child("plot2").child("day2")
        val dbDay3P2 = database.child("settings").child("plot2").child("day3")
        val dbDay4P2 = database.child("settings").child("plot2").child("day4")
        val dbDay5P2 = database.child("settings").child("plot2").child("day5")
        val dbDay6P2 = database.child("settings").child("plot2").child("day6")
        val dbDay7P2 = database.child("settings").child("plot2").child("day7")

        val dbDay1Lamp = database.child("settings").child("lamp").child("day1")
        val dbDay2Lamp = database.child("settings").child("lamp").child("day2")
        val dbDay3Lamp = database.child("settings").child("lamp").child("day3")
        val dbDay4Lamp = database.child("settings").child("lamp").child("day4")
        val dbDay5Lamp = database.child("settings").child("lamp").child("day5")
        val dbDay6Lamp = database.child("settings").child("lamp").child("day6")
        val dbDay7Lamp = database.child("settings").child("lamp").child("day7")

        val dbAutoPlot1 = database.child("control").child("auto-plot1")
        val dbAutoPlot2 = database.child("control").child("auto-plot2")
        val dbAutoLamp = database.child("control").child("auto-lamp")

        // Time 1 P1
        dbTime1P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTime1P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Time 2 P1
        dbTime2P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTime2P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Time 1 P2
        dbTime1P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTime1P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Time 2 P2
        dbTime2P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTime2P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        // Duration 1 P1
        dbDuration1P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDuration1P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Duration 2 P1
        dbDuration2P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDuration2P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Duration 1 P2
        dbDuration1P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDuration1P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Duration 2 P2
        dbDuration2P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDuration2P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        // Unit 1 P1
        dbUnit1P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataUnit1P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Unit 2 P1
        dbUnit2P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataUnit2P1 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Unit 1 P2
        dbUnit1P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataUnit1P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Unit 2 P2
        dbUnit2P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataUnit2P2 = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        // Day 1 P1
        dbDay1P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay1P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 2 P1
        dbDay2P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay2P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 3 P1
        dbDay3P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay3P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 4 P1
        dbDay4P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay4P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 5 P1
        dbDay5P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay5P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 6 P1
        dbDay6P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay6P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 7 P1
        dbDay7P1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay7P1 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        // Day 1 P2
        dbDay1P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay1P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 2 P2
        dbDay2P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay2P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 3 P2
        dbDay3P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay3P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 4 P2
        dbDay4P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay4P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 5 P2
        dbDay5P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay5P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 6 P2
        dbDay6P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay6P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 7 P2
        dbDay7P2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay7P2 = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        // Time On Lamp
        dbTimeOnLamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTimeOnLamp = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Time Off Lamp
        dbTimeOffLamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataTimeOffLamp = dataSnapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 1 Lamp
        dbDay1Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay1Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 2 Lamp
        dbDay2Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay2Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 3 Lamp
        dbDay3Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay3Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 4 Lamp
        dbDay4Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay4Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 5 Lamp
        dbDay5Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay5Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 6 Lamp
        dbDay6Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay6Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Day 7 Lamp
        dbDay7Lamp.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataDay7Lamp = dataSnapshot.value as Boolean
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })

        val pref = PrefManager(this)
        val tvStateAutoP1 = findViewById<TextView>(R.id.tv_state_auto_p1)
        val tvStateAutoP2 = findViewById<TextView>(R.id.tv_state_auto_p2)
        val tvStateAutoLamp = findViewById<TextView>(R.id.tv_state_auto_lamp)

        val btnPlot1 = findViewById<ToggleButton>(R.id.btn_plot1)
        val btnPlot2 = findViewById<ToggleButton>(R.id.btn_plot2)
        val btnLamp = findViewById<ToggleButton>(R.id.btn_lamp)

        // Auto P1
        dbAutoPlot1.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pref.stateAutoP1 = dataSnapshot.value as Boolean
                if(pref.stateAutoP1 == true){
                    tvStateAutoP1.text = "Auto ON"
                    btnPlot1.isEnabled = false
                    btnPlot1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_60))
                }else{
                    tvStateAutoP1.text = "Auto OFF"
                    btnPlot1.isEnabled = true
                    btnPlot1.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.primary))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Auto P2
        dbAutoPlot2.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pref.stateAutoP2 = dataSnapshot.value as Boolean
                if(pref.stateAutoP2 == true){
                    tvStateAutoP2.text = "Auto ON"
                    btnPlot2.isEnabled = false
                    btnPlot2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_60))
                }else{
                    tvStateAutoP2.text = "Auto OFF"
                    btnPlot2.isEnabled = true
                    btnPlot2.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.primary))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
        // Auto Lamp
        dbAutoLamp.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pref.stateAutoLamp = dataSnapshot.value as Boolean
                if(pref.stateAutoLamp == true){
                    tvStateAutoLamp.text = "Auto ON"
                    btnLamp.isEnabled = false
                    btnLamp.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_60))
                }else{
                    tvStateAutoLamp.text = "Auto OFF"
                    btnLamp.isEnabled = true
                    btnLamp.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.primary))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Failed to read value", error.toException())
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun settingsPlot1(){
        val btnPlot1 = findViewById<ToggleButton>(R.id.btn_plot1)

        val dbTime1 = database.child("settings").child("plot1").child("time1")
        val dbTime2 = database.child("settings").child("plot1").child("time2")

        val dbDuration1 = database.child("settings").child("plot1").child("duration1")
        val dbDuration2 = database.child("settings").child("plot1").child("duration2")

        val dbUnit1 = database.child("settings").child("plot1").child("unit1")
        val dbUnit2 = database.child("settings").child("plot1").child("unit2")

        val dbDay1 = database.child("settings").child("plot1").child("day1")
        val dbDay2 = database.child("settings").child("plot1").child("day2")
        val dbDay3 = database.child("settings").child("plot1").child("day3")
        val dbDay4 = database.child("settings").child("plot1").child("day4")
        val dbDay5 = database.child("settings").child("plot1").child("day5")
        val dbDay6 = database.child("settings").child("plot1").child("day6")
        val dbDay7 = database.child("settings").child("plot1").child("day7")

        val dbSet = database.child("control").child("state")

        btnPlot1.setOnLongClickListener {
            val dialogSheet = DialogSheet(this)
            dialogSheet.setView(R.layout.bs_plot_1)
            val inflatedView = dialogSheet.inflatedView

            val btnTime1 = inflatedView?.findViewById<TextView>(R.id.btn_time1_p1)
            val btnTime2 = inflatedView?.findViewById<TextView>(R.id.btn_time2_p1)

            val tvDuration1 = inflatedView?.findViewById<TextView>(R.id.tv_duration1_p1)
            val tvDuration2 = inflatedView?.findViewById<TextView>(R.id.tv_duration2_p1)

            val btnUnit1 = inflatedView?.findViewById<ToggleButton>(R.id.btn_unit1_p1)
            val btnUnit2 = inflatedView?.findViewById<ToggleButton>(R.id.btn_unit2_p1)

            val btnDay1 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day1_p1)
            val btnDay2 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day2_p1)
            val btnDay3 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day3_p1)
            val btnDay4 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day4_p1)
            val btnDay5 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day5_p1)
            val btnDay6 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day6_p1)
            val btnDay7 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day7_p1)

            val btnSet = inflatedView?.findViewById<Button>(R.id.btn_set_p1)

            btnTime1?.text = dataTime1P1
            btnTime2?.text = dataTime2P1
            tvDuration1?.text = dataDuration1P1
            tvDuration2?.text = dataDuration2P1

            btnUnit1?.isChecked = dataUnit1P1 == "second"
            btnUnit2?.isChecked = dataUnit2P1 == "second"

            btnDay1?.isChecked = dataDay1P1
            btnDay2?.isChecked = dataDay2P1
            btnDay3?.isChecked = dataDay3P1
            btnDay4?.isChecked = dataDay4P1
            btnDay5?.isChecked = dataDay5P1
            btnDay6?.isChecked = dataDay6P1
            btnDay7?.isChecked = dataDay7P1

            // Set Time to database
            btnTime1?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTime1 = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTime1.text = mSetTime1
                    dbTime1.setValue(mSetTime1)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }
            btnTime2?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTime2 = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTime2.text = mSetTime2
                    dbTime2.setValue(mSetTime2)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }

            // Set Unit 1
            btnUnit1?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbUnit1.setValue("second")
                } else {
                    dbUnit1.setValue("minute")
                }
            }
            // Set Unit 2
            btnUnit2?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbUnit2.setValue("second")
                } else {
                    dbUnit2.setValue("minute")
                }
            }

            // Set day 1
            btnDay1?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay1.setValue(true)
                } else {
                    dbDay1.setValue(false)
                }
            }
            // Set day 2
            btnDay2?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay2.setValue(true)
                } else {
                    dbDay2.setValue(false)
                }
            }
            // Set day 3
            btnDay3?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay3.setValue(true)
                } else {
                    dbDay3.setValue(false)
                }
            }
            // Set day 4
            btnDay4?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay4.setValue(true)
                } else {
                    dbDay4.setValue(false)
                }
            }
            // Set day 5
            btnDay5?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay5.setValue(true)
                } else {
                    dbDay5.setValue(false)
                }
            }
            // Set day 6
            btnDay6?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay6.setValue(true)
                } else {
                    dbDay6.setValue(false)
                }
            }
            // Set day 7
            btnDay7?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay7.setValue(true)
                } else {
                    dbDay7.setValue(false)
                }
            }

            btnSet?.setOnClickListener {
                var mDuration1 = tvDuration1?.text.toString()
                var mDuration2 = tvDuration2?.text.toString()

                if (mDuration1.isEmpty()){
                    mDuration1 = "0"
                }
                if (mDuration2.isEmpty()){
                    mDuration2 = "0"
                }

                dbDuration1.setValue(mDuration1.toInt())
                dbDuration2.setValue(mDuration2.toInt())
                dbSet.setValue("set-change")
                Toast.makeText(this, "Menyimpan pengaturan", Toast.LENGTH_SHORT).show()
            }

            dialogSheet.show()
            false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun settingsPlot2(){
        val btnPlot2 = findViewById<ToggleButton>(R.id.btn_plot2)

        val dbTime1 = database.child("settings").child("plot2").child("time1")
        val dbTime2 = database.child("settings").child("plot2").child("time2")

        val dbDuration1 = database.child("settings").child("plot2").child("duration1")
        val dbDuration2 = database.child("settings").child("plot2").child("duration2")

        val dbUnit1 = database.child("settings").child("plot2").child("unit1")
        val dbUnit2 = database.child("settings").child("plot2").child("unit2")

        val dbDay1 = database.child("settings").child("plot2").child("day1")
        val dbDay2 = database.child("settings").child("plot2").child("day2")
        val dbDay3 = database.child("settings").child("plot2").child("day3")
        val dbDay4 = database.child("settings").child("plot2").child("day4")
        val dbDay5 = database.child("settings").child("plot2").child("day5")
        val dbDay6 = database.child("settings").child("plot2").child("day6")
        val dbDay7 = database.child("settings").child("plot2").child("day7")

        val dbSet = database.child("control").child("state")

        btnPlot2.setOnLongClickListener {
            val dialogSheet = DialogSheet(this)
            dialogSheet.setView(R.layout.bs_plot_2)
            val inflatedView = dialogSheet.inflatedView

            val btnTime1 = inflatedView?.findViewById<TextView>(R.id.btn_time1_p2)
            val btnTime2 = inflatedView?.findViewById<TextView>(R.id.btn_time2_p2)

            val tvDuration1 = inflatedView?.findViewById<TextView>(R.id.tv_duration1_p2)
            val tvDuration2 = inflatedView?.findViewById<TextView>(R.id.tv_duration2_p2)

            val btnUnit1 = inflatedView?.findViewById<ToggleButton>(R.id.btn_unit1_p2)
            val btnUnit2 = inflatedView?.findViewById<ToggleButton>(R.id.btn_unit2_p2)

            val btnDay1 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day1_p2)
            val btnDay2 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day2_p2)
            val btnDay3 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day3_p2)
            val btnDay4 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day4_p2)
            val btnDay5 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day5_p2)
            val btnDay6 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day6_p2)
            val btnDay7 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day7_p2)

            val btnSet = inflatedView?.findViewById<Button>(R.id.btn_set_p2)

            btnTime1?.text = dataTime1P2
            btnTime2?.text = dataTime2P2
            tvDuration1?.text = dataDuration1P2
            tvDuration2?.text = dataDuration2P2

            btnUnit1?.isChecked = dataUnit1P2 == "second"
            btnUnit2?.isChecked = dataUnit2P2 == "second"

            btnDay1?.isChecked = dataDay1P2
            btnDay2?.isChecked = dataDay2P2
            btnDay3?.isChecked = dataDay3P2
            btnDay4?.isChecked = dataDay4P2
            btnDay5?.isChecked = dataDay5P2
            btnDay6?.isChecked = dataDay6P2
            btnDay7?.isChecked = dataDay7P2

            // Set Time to database
            btnTime1?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTime1 = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTime1.text = mSetTime1
                    dbTime1.setValue(mSetTime1)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }
            btnTime2?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTime2 = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTime2.text = mSetTime2
                    dbTime2.setValue(mSetTime2)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }

            // Set Unit 1
            btnUnit1?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbUnit1.setValue("second")
                } else {
                    dbUnit1.setValue("minute")
                }
            }
            // Set Unit 2
            btnUnit2?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbUnit2.setValue("second")
                } else {
                    dbUnit2.setValue("minute")
                }
            }

            // Set day 1
            btnDay1?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay1.setValue(true)
                } else {
                    dbDay1.setValue(false)
                }
            }
            // Set day 2
            btnDay2?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay2.setValue(true)
                } else {
                    dbDay2.setValue(false)
                }
            }
            // Set day 3
            btnDay3?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay3.setValue(true)
                } else {
                    dbDay3.setValue(false)
                }
            }
            // Set day 4
            btnDay4?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay4.setValue(true)
                } else {
                    dbDay4.setValue(false)
                }
            }
            // Set day 5
            btnDay5?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay5.setValue(true)
                } else {
                    dbDay5.setValue(false)
                }
            }
            // Set day 6
            btnDay6?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay6.setValue(true)
                } else {
                    dbDay6.setValue(false)
                }
            }
            // Set day 7
            btnDay7?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay7.setValue(true)
                } else {
                    dbDay7.setValue(false)
                }
            }


            btnSet?.setOnClickListener {
                var mDuration1 = tvDuration1?.text.toString()
                var mDuration2 = tvDuration2?.text.toString()

                if (mDuration1.isEmpty()){
                    mDuration1 = "0"
                }
                if (mDuration2.isEmpty()){
                    mDuration2 = "0"
                }

                dbDuration1.setValue(mDuration1.toInt())
                dbDuration2.setValue(mDuration2.toInt())
                dbSet.setValue("set-change")
                Toast.makeText(this, "Menyimpan pengaturan", Toast.LENGTH_SHORT).show()
            }

            dialogSheet.show()
            false
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun settingsLamp(){
        val btnLamp = findViewById<ToggleButton>(R.id.btn_lamp)

        val dbTimeOnLamp = database.child("settings").child("lamp").child("time-on")
        val dbTimeOffLamp = database.child("settings").child("lamp").child("time-off")

        val dbDay1 = database.child("settings").child("lamp").child("day1")
        val dbDay2 = database.child("settings").child("lamp").child("day2")
        val dbDay3 = database.child("settings").child("lamp").child("day3")
        val dbDay4 = database.child("settings").child("lamp").child("day4")
        val dbDay5 = database.child("settings").child("lamp").child("day5")
        val dbDay6 = database.child("settings").child("lamp").child("day6")
        val dbDay7 = database.child("settings").child("lamp").child("day7")

        val dbSet = database.child("control").child("state")

        btnLamp.setOnLongClickListener {
            val dialogSheet = DialogSheet(this)
            dialogSheet.setView(R.layout.bs_lamp)
            val inflatedView = dialogSheet.inflatedView

            val btnTimeOn = inflatedView?.findViewById<TextView>(R.id.btn_time_on_lightning)
            val btnTimeOff = inflatedView?.findViewById<TextView>(R.id.btn_time_off_lightning)

            val btnDay1 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day1_lamp)
            val btnDay2 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day2_lamp)
            val btnDay3 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day3_lamp)
            val btnDay4 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day4_lamp)
            val btnDay5 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day5_lamp)
            val btnDay6 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day6_lamp)
            val btnDay7 = inflatedView?.findViewById<SwitchMaterial>(R.id.btn_day7_lamp)

            val btnSet = inflatedView?.findViewById<Button>(R.id.btn_set_lamp)

            btnTimeOn?.text = dataTimeOnLamp
            btnTimeOff?.text = dataTimeOffLamp

            btnDay1?.isChecked = dataDay1Lamp
            btnDay2?.isChecked = dataDay2Lamp
            btnDay3?.isChecked = dataDay3Lamp
            btnDay4?.isChecked = dataDay4Lamp
            btnDay5?.isChecked = dataDay5Lamp
            btnDay6?.isChecked = dataDay6Lamp
            btnDay7?.isChecked = dataDay7Lamp

            // Set Time On to database
            btnTimeOn?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTimeOn = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTimeOn.text = mSetTimeOn
                    dbTimeOnLamp.setValue(mSetTimeOn)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }
            // Set Time Off to database
            btnTimeOff?.setOnClickListener{
                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    val mSetTimeOff = SimpleDateFormat("HH:mm").format(cal.time).toString()
                    btnTimeOff.text = mSetTimeOff
                    dbTimeOffLamp.setValue(mSetTimeOff)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE), true).show()
            }

            // Set day 1
            btnDay1?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay1.setValue(true)
                } else {
                    dbDay1.setValue(false)
                }
            }
            // Set day 2
            btnDay2?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay2.setValue(true)
                } else {
                    dbDay2.setValue(false)
                }
            }
            // Set day 3
            btnDay3?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay3.setValue(true)
                } else {
                    dbDay3.setValue(false)
                }
            }
            // Set day 4
            btnDay4?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay4.setValue(true)
                } else {
                    dbDay4.setValue(false)
                }
            }
            // Set day 5
            btnDay5?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay5.setValue(true)
                } else {
                    dbDay5.setValue(false)
                }
            }
            // Set day 6
            btnDay6?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay6.setValue(true)
                } else {
                    dbDay6.setValue(false)
                }
            }
            // Set day 7
            btnDay7?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    dbDay7.setValue(true)
                } else {
                    dbDay7.setValue(false)
                }
            }


            btnSet?.setOnClickListener {
                dbSet.setValue("set-change")
                Toast.makeText(this, "Menyimpan pengaturan", Toast.LENGTH_SHORT).show()
            }

            dialogSheet.show()
            false
        }
    }

    private fun chart() {
        val shimmerGraph = findViewById<ShimmerFrameLayout>(R.id.shimmer_graph)
        val chartBox = findViewById<LinearLayout>(R.id.chart_box)

        shimmerGraph.visibility = View.VISIBLE
        chartBox.visibility = View.GONE

        val mChart = findViewById<LineChart>(R.id.chart)

        val dataGraph = arrayListOf<ChartModel>()
        val ref = FirebaseDatabase.getInstance().getReference("data")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(!graphState){
                    if(dataSnapshot.exists()) {
                        shimmerGraph.visibility = View.GONE
                        chartBox.visibility = View.VISIBLE
                    }else{
                        shimmerGraph.visibility = View.VISIBLE
                        chartBox.visibility = View.GONE
                    }

                    for (dataGraphSnapshot in dataSnapshot.children) {
                        val lineChart = dataGraphSnapshot.getValue(ChartModel::class.java)
                        dataGraph.add(lineChart!!)
                    }
                    val arr = JSONArray(dataGraph)
                    val y1Val = ArrayList<Entry>()
                    val y2Val = ArrayList<Entry>()
                    val y3Val = ArrayList<Entry>()
                    val y4Val = ArrayList<Entry>()
                    for (i in 0 until arr.length()) {
                        val x = dataGraph[i].id
                        val y1 = dataGraph[i].temperature
                        val y2 = dataGraph[i].humidity
                        val y3 = dataGraph[i].plot1
                        val y4 = dataGraph[i].plot2
                        // y data
                        // x banyak data
                        y1Val.add(Entry(x.toString().toFloat(), y1.toString().toFloat()))
                        y2Val.add(Entry(x.toString().toFloat(), y2.toString().toFloat()))
                        y3Val.add(Entry(x.toString().toFloat(), y3.toString().toFloat()))
                        y4Val.add(Entry(x.toString().toFloat(), y4.toString().toFloat()))
                    }

                    mChart.animateX(2500)
                    mChart.setTouchEnabled(true)
                    mChart.setPinchZoom(true)
                    mChart.xAxis.setDrawGridLines(false)
                    mChart.axisRight.setDrawGridLines(false)
                    mChart.axisLeft.setDrawGridLines(false)
                    //mChart.setDrawMarkers(true)

                    val set1 = LineDataSet(y1Val, "Suhu Udara ")
                    val set2 = LineDataSet(y2Val, "Kel. Udara ")
                    val set3 = LineDataSet(y3Val, "Kel. Plot 1")
                    val set4 = LineDataSet(y4Val, "Kel. Plot 2")

                    set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    set1.formLineWidth  = 1f
                    set1.setDrawCircles(false)
                    set1.setDrawValues(false) // Nilai poin xy
                    set1.setDrawFilled(false) // Bayangan bawah garis
                    set1.lineWidth = 2f
                    set1.circleHoleRadius = 6f
                    //set1.valueTextSize = 8f
                    set1.highLightColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_orange)
                    set1.color = ContextCompat.getColor(this@DashboardActivity,R.color.base_orange)
                    set1.fillColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_orange)
                    set1.setCircleColor(ContextCompat.getColor(this@DashboardActivity,R.color.base_orange))
                    set1.fillAlpha = 100
                    set1.setDrawHorizontalHighlightIndicator(false)
                    set1.axisDependency = YAxis.AxisDependency.LEFT

                    set2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    set2.formLineWidth  = 1f
                    set2.setDrawCircles(false)
                    set2.setDrawValues(false) // Nilai poin xy
                    set2.setDrawFilled(false) // Bayangan bawah garis
                    set2.lineWidth = 2f
                    set2.circleHoleRadius = 6f
                    ///set2.valueTextSize = 8f
                    set2.highLightColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_purple)
                    set2.color = ContextCompat.getColor(this@DashboardActivity,R.color.base_purple)
                    set2.fillColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_purple)
                    set2.setCircleColor(ContextCompat.getColor(this@DashboardActivity,R.color.base_purple))
                    set2.fillAlpha = 100
                    set2.setDrawHorizontalHighlightIndicator(false)
                    set2.axisDependency = YAxis.AxisDependency.LEFT


                    set3.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    set3.formLineWidth  = 1f
                    set3.setDrawCircles(false)
                    set3.setDrawValues(false) // Nilai poin xy
                    set3.setDrawFilled(false) // Bayangan bawah garis
                    set3.lineWidth = 2f
                    set3.circleHoleRadius = 6f
                    //set3.valueTextSize = 8f
                    set3.highLightColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_red)
                    set3.color = ContextCompat.getColor(this@DashboardActivity,R.color.base_red)
                    set3.fillColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_red)
                    set3.setCircleColor(ContextCompat.getColor(this@DashboardActivity,R.color.base_red))
                    set3.fillAlpha = 100
                    set3.setDrawHorizontalHighlightIndicator(false)
                    set3.axisDependency = YAxis.AxisDependency.LEFT

                    set4.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    set4.formLineWidth  = 1f
                    set4.setDrawCircles(false)
                    set4.setDrawValues(false) // Nilai poin xy
                    set4.setDrawFilled(false) // Bayangan bawah garis
                    set4.lineWidth = 2f
                    set4.circleHoleRadius = 6f
                    //set4.valueTextSize = 8f
                    set4.highLightColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_green)
                    set4.color = ContextCompat.getColor(this@DashboardActivity,R.color.base_green)
                    set4.fillColor = ContextCompat.getColor(this@DashboardActivity,R.color.base_green)
                    set4.setCircleColor(ContextCompat.getColor(this@DashboardActivity,R.color.base_green))
                    set4.fillAlpha = 100
                    set4.setDrawHorizontalHighlightIndicator(false)
                    set4.axisDependency = YAxis.AxisDependency.LEFT

                    val marker : IMarker = MarkerView(this@DashboardActivity, mChart, R.layout.marker_tree_item)
                    mChart.marker = marker


                    val dataSets = ArrayList<ILineDataSet>()
                    dataSets.add(set1)
                    dataSets.add(set2)
                    dataSets.add(set3)
                    dataSets.add(set4)

                    val data1 = LineData(dataSets)

                    mChart.data = data1
                    mChart.description.isEnabled = false
                    mChart.legend.entries
                    mChart.setPinchZoom(true)
                    mChart.legend.isEnabled = true
                    mChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                    mChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    mChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
                    mChart.legend.setDrawInside(false)
                    mChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    mChart.xAxis.labelCount = 11
                }
                graphState = true
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val i = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(i)
            }
            R.id.information -> {
                val i = Intent(applicationContext, InfoActivity::class.java)
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}