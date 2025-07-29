package com.jamie.businessideasevaluator.View.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.Data.SD.SharedPreferenceManager
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.ActivityTutorialCompletedBinding

class TutorialCompleted : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialCompletedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTutorialCompletedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         val businessTags = mapOf(
            "Business Analyse" to 45,
            "Personal Skills" to 25,
             "Own Criteria" to 30,
        )
        setupPieChart(binding.businessPieChart, businessTags )
        val prefManager = SharedPreferenceManager(this)
        binding.okayBtn.setOnClickListener {
            prefManager.setTutorialCompleted()
           val intent = Intent(this, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)

        }
    }

    private fun setupPieChart(pieChart: PieChart, tags: Map<String, Int>) {
        if (tags.isEmpty()) return

        val entries = tags.map { PieEntry(it.value.toFloat()) }

        val dataSet = PieDataSet(entries, "")


        val context = pieChart.context
        val colors = listOf(
            ContextCompat.getColor(context, R.color.offYellow),
            ContextCompat.getColor(context, R.color.tan),
            ContextCompat.getColor(context, R.color.paleOrange)
        )


        val repeatedColors = List(entries.size) { index -> colors[index % colors.size] }

        dataSet.colors = repeatedColors
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 0f

        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String = ""
        }

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(false)

        pieChart.setDrawHoleEnabled(true)
        pieChart.holeRadius = 70f
        pieChart.transparentCircleRadius = 75f

        pieChart.invalidate()
    }
}