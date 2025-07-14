package com.jamie.businessideasevaluator.View.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.BusinessPageBusinessRecyclerAdapter
import com.jamie.businessideasevaluator.View.Adapters.BusinessPageOwnRecyclerAdapter
import com.jamie.businessideasevaluator.View.Adapters.BusinessPagePersonalRecyclerAdapter
import com.jamie.businessideasevaluator.View.Adapters.HomeRecAdapter
import com.jamie.businessideasevaluator.ViewModel.BusinessPageViewModel
import com.jamie.businessideasevaluator.ViewModel.HomeViewModel
import com.jamie.businessideasevaluator.databinding.ActivityBusinessPageBinding
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding
import kotlin.getValue


class BusinessPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessPageBinding
    private val viewModel: BusinessPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.initRepository(this)
        val businessPosition = intent.getIntExtra("idea_position",-1)
        val fragment = intent.getIntExtra("fragment",-1)

        if (businessPosition != -1) {
            when (fragment) {
                1 -> {
                    viewModel.loadIdeaByPosition(businessPosition)
                }

                2 -> {
                    viewModel.loadRankedIdeaByPosition(businessPosition)
                }
            }
        }

        viewModel.selectedIdea.observe(this) { idea ->
            binding.dailyQuote.text = idea.businessName
            binding.descriptionTxt.text = idea.businessDescription

            val adapterBusiness = BusinessPageBusinessRecyclerAdapter(idea)
            binding.businessAnalysisRec.adapter = adapterBusiness
            binding.businessAnalysisRec.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
            val adapterPersonal = BusinessPagePersonalRecyclerAdapter(idea)
            binding.personalSkillsRec.adapter = adapterPersonal
            binding.personalSkillsRec.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
            val adapterOwnCriteria = BusinessPageOwnRecyclerAdapter(idea)
            binding.ownCriteriaRec.adapter = adapterOwnCriteria
            binding.ownCriteriaRec.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)

            setupPieChart(binding.businessPieChart,idea.businessTags)

            // Gets tag values and total
            val tagEntries = idea.businessTags.entries.toList()
            val total = tagEntries.sumOf { it.value }


            val textViews = listOf(binding.textOne, binding.textTwo, binding.textThree)

            for (i in 0 until minOf(tagEntries.size, 3)) {
                val label = tagEntries[i].key
                val value = tagEntries[i].value.toFloat()
                val percent = (value / total) * 100
                textViews[i].text = "$label: ${"%.0f".format(percent)}%"
            }

            for (i in tagEntries.size until 3) {
                textViews[i].text = ""
            }
            val rank = viewModel.getBusinessIdeaRank(idea)
            binding.numberButton.text = "#${rank}"
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

        // Repeat or cycle colors if needed
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