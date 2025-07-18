package com.healthtrackingapp.screen

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.healthtrackingapp.data.HealthMetric

@Composable
fun MetricLineChart(
    modifier: Modifier = Modifier,
    data: List<HealthMetric>,
    label: String
) {
    val entries = data.sortedBy { it.timestamp }.mapIndexed { index, metric ->
        Entry(index.toFloat(), metric.value)
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setTouchEnabled(true)
                setPinchZoom(true)
                description.isEnabled = false

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisLeft.setDrawGridLines(false)
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, label).apply {
                color = AndroidColor.BLUE
                valueTextColor = AndroidColor.BLACK
                lineWidth = 2f
                setDrawCircles(true)
                setDrawValues(false)
            }

            chart.data = LineData(dataSet)
            chart.invalidate() // 🔄 Forces chart redraw
        }
    )
}


