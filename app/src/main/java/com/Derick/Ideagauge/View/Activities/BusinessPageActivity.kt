package com.Derick.Ideagauge.View.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.Derick.Ideagauge.R
import com.Derick.Ideagauge.View.Adapters.BusinessPageBusinessRecyclerAdapter
import com.Derick.Ideagauge.View.Adapters.BusinessPageOwnRecyclerAdapter
import com.Derick.Ideagauge.View.Adapters.BusinessPagePersonalRecyclerAdapter
import com.Derick.Ideagauge.ViewModel.BusinessPageViewModel
import com.Derick.Ideagauge.databinding.ActivityBusinessPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue


class BusinessPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessPageBinding
    private val viewModel: BusinessPageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        binding = ActivityBusinessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lateinit var prompt:String
        val shimmerLayout = binding.aiShimmer
        val aiTipText = binding.aiTipText
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewModel.initRepository(this)
        val dateId = intent.getLongExtra("DateId", 0)
        val businessPosition = intent.getIntExtra("idea_position", -1)
        val fragment = intent.getIntExtra("fragment", -1)

        if (businessPosition != -1) {
            when (fragment) {
                1 -> {
                    viewModel.loadIdeaByPosition(businessPosition)
                }

                2 -> {
                    viewModel.loadIdeaByDateId(dateId)
                }
            }
        }

        viewModel.selectedIdea.observe(this) { idea ->
            binding.dailyQuote.text = idea.businessName
            binding.descriptionTxt.text = idea.businessDescription

            val adapterBusiness = BusinessPageBusinessRecyclerAdapter(idea)
            binding.businessAnalysisRec.adapter = adapterBusiness
            binding.businessAnalysisRec.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val adapterPersonal = BusinessPagePersonalRecyclerAdapter(idea)
            binding.personalSkillsRec.adapter = adapterPersonal
            binding.personalSkillsRec.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            val adapterOwnCriteria = BusinessPageOwnRecyclerAdapter(idea)
            binding.ownCriteriaRec.adapter = adapterOwnCriteria
            binding.ownCriteriaRec.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            setupPieChart(binding.businessPieChart, idea.businessTags)


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

            prompt = """
    My business is called "${idea.businessName}". It is described as follows: "${idea.businessDescription}".
    
    Based on this, provide me with one paragraph of advice to improve or grow this business idea, talk about how the market for said business is.
    Be insightful and practical. i want to be maximum of 80 words. after give three bullet points that can help the business
""".trimIndent()

        }




        runOnUiThread {
            aiTipText.visibility = View.INVISIBLE
            shimmerLayout.visibility = View.VISIBLE
            shimmerLayout.startShimmer()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val model = Firebase.ai(backend = GenerativeBackend.googleAI())
                    .generativeModel("gemini-2.5-flash")

                val response = model.generateContent(prompt)
                Log.d(TAG, "onCreate: $response")

                // Update UI on main thread
                runOnUiThread {
                    aiTipText.text = response.text ?: "No response"
                    aiTipText.visibility = View.VISIBLE
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE

                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    aiTipText.text = "Failed to load AI tip"
                    aiTipText.visibility = View.VISIBLE
                }
            }
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
    companion object{
        private const val TAG = "BusinessPageActivity"
    }
}