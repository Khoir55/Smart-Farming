package com.ei.smart_farming.ui.main.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.ei.smart_farming.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class MarkerView(context: Context?, private val lineChart: LineChart, layoutResource: Int) : MarkerView(context, layoutResource),
    IMarker {
    private val square1 = findViewById<TextView>(R.id.square1)
    private val square2 = findViewById<TextView>(R.id.square2)
    private val square3 = findViewById<TextView>(R.id.square3)
    private val square4 = findViewById<TextView>(R.id.square4)
    private val item1 = findViewById<TextView>(R.id.item1)
    private val item2 = findViewById<TextView>(R.id.item2)
    private val item3 = findViewById<TextView>(R.id.item3)
    private val item4 = findViewById<TextView>(R.id.item4)

    override fun refreshContent(e: Entry, highlight: Highlight) {
        try {
            square1.setBackgroundColor(lineChart.data.getDataSetByIndex(0).color)
            square2.setBackgroundColor(lineChart.data.getDataSetByIndex(1).color)
            square3.setBackgroundColor(lineChart.data.getDataSetByIndex(2).color)
            square4.setBackgroundColor(lineChart.data.getDataSetByIndex(3).color)
            val val1 = lineChart.data.getDataSetByIndex(0).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
            val val2 = lineChart.data.getDataSetByIndex(1).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
            val val3 = lineChart.data.getDataSetByIndex(2).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
            val val4 = lineChart.data.getDataSetByIndex(3).getEntryForXValue(e.x, Float.NaN, DataSet.Rounding.CLOSEST) as Entry
            item1.text = String.format("%,.0f", val1.y)
            item2.text = String.format("%,.0f", val2.y)
            item3.text = String.format("%,.0f", val3.y)
            item4.text = String.format("%,.0f", val4.y)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        super.refreshContent(e, highlight)
    }

    private var mOffset: MPPointF? = null
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }
}