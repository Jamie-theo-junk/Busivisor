package com.Derick.Ideagauge.View.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.Derick.Ideagauge.R
import com.Derick.Ideagauge.View.Adapters.AdsManager
import com.Derick.Ideagauge.View.Adapters.BusinessPageBusinessRecyclerAdapter
import com.Derick.Ideagauge.View.Adapters.BusinessPageOwnRecyclerAdapter
import com.Derick.Ideagauge.View.Adapters.BusinessPagePersonalRecyclerAdapter
import com.Derick.Ideagauge.ViewModel.BusinessPageViewModel
import com.Derick.Ideagauge.databinding.ActivityBusinessPageBinding
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale

class BusinessPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusinessPageBinding
    private val viewModel: BusinessPageViewModel by viewModels()

    private var triedFirstAdShow = false
    private var aiRequestedOnce = false  // only fetch once per open

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        binding = ActivityBusinessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Warm up an interstitial (optional)
        AdsManager.preload(this)

        // Insets + theme
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Load target idea
        viewModel.initRepository(this)
        val dateId = intent.getLongExtra("DateId", 0L)
        val pos = intent.getIntExtra("idea_position", -1)
        val frag = intent.getIntExtra("fragment", -1)
        if (pos != -1) {
            when (frag) {
                1 -> viewModel.loadIdeaByPosition(pos)
                2 -> viewModel.loadIdeaByDateId(dateId)
            }
        }

        // Observe idea; show stored advice or fetch once
        viewModel.selectedIdea.observe(this) { idea ->
            renderIdea(idea)

            if (idea.aiSuggestion.isNotBlank()) {
                showAiSuggestion(idea.aiSuggestion)
            } else if (!aiRequestedOnce) {
                aiRequestedOnce = true
                fetchAndSaveAdviceOnce(idea)
            } else {
                // safety
                stopShimmer()
                binding.aiTipText.apply {
                    text = "No response"
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val wantAdNow = intent.getBooleanExtra("show_interstitial_now", false)
        if (wantAdNow && !triedFirstAdShow) {
            triedFirstAdShow = true
            AdsManager.showIfReady(this)
            intent.removeExtra("show_interstitial_now")
        }
    }

    // --------- SIMPLE AI FLOW ---------

    private fun fetchAndSaveAdviceOnce(idea: BusinessIdea) {
        startShimmer()

        val prompt = buildPrompt(idea)

        CoroutineScope(Dispatchers.IO).launch {
            var suggestion = ""
            try {
                val model = com.google.firebase.Firebase.ai(backend = GenerativeBackend.googleAI())
                    .generativeModel("gemini-2.5-flash")

                val resp = model.generateContent(prompt)
                suggestion = resp.text?.trim().orEmpty()

                // Save to DB (first time only)
                try {
                    DbHelper(this@BusinessPageActivity)
                        .updateAiSuggestionByName(idea.businessName, suggestion)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save AI suggestion", e)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Gemini error", e)
                suggestion = ""
            } finally {
                runOnUiThread {
                    stopShimmer()
                    binding.aiTipText.apply {
                        text = suggestion.ifBlank { "No response" }
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun startShimmer() {
        binding.aiTipText.visibility = View.INVISIBLE
        binding.aiShimmer.visibility = View.VISIBLE
        binding.aiShimmer.startShimmer()
    }

    private fun stopShimmer() {
        binding.aiShimmer.stopShimmer()
        binding.aiShimmer.visibility = View.GONE
    }

    private fun showAiSuggestion(text: String) {
        stopShimmer()
        binding.aiTipText.apply {
            this.text = text
            visibility = View.VISIBLE
        }
    }

    // --------- UI ---------

    private fun renderIdea(idea: BusinessIdea) {
        binding.dailyQuote.text = idea.businessName
        binding.descriptionTxt.text = idea.businessDescription

        // Delete dialog with your original colors
        binding.deleteBtnCard.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete this idea?")
                .setPositiveButton("Yes", null)
                .setNegativeButton("Cancel", null)
                .create()

            dialog.setOnShowListener {
                val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                positive.setTextColor(ContextCompat.getColor(this, R.color.dark_orange))
                negative.setTextColor(ContextCompat.getColor(this, R.color.grey))

                positive.setOnClickListener {
                    viewModel.deleteBusinessIdea(idea)
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

        // Horizontal lists
        binding.businessAnalysisRec.apply {
            adapter = BusinessPageBusinessRecyclerAdapter(idea)
            layoutManager = LinearLayoutManager(
                this@BusinessPageActivity, LinearLayoutManager.HORIZONTAL, false
            )
        }
        binding.personalSkillsRec.apply {
            adapter = BusinessPagePersonalRecyclerAdapter(idea)
            layoutManager = LinearLayoutManager(
                this@BusinessPageActivity, LinearLayoutManager.HORIZONTAL, false
            )
        }
        binding.ownCriteriaRec.apply {
            adapter = BusinessPageOwnRecyclerAdapter(idea)
            layoutManager = LinearLayoutManager(
                this@BusinessPageActivity, LinearLayoutManager.HORIZONTAL, false
            )
        }

        // Chart + top-3 tags
        setupPieChart(binding.businessPieChart, idea.businessTags)
        val entries = idea.businessTags.entries.toList()
        val total = entries.sumOf { it.value }.coerceAtLeast(1)
        val tvs = listOf(binding.textOne, binding.textTwo, binding.textThree)
        for (i in 0 until minOf(entries.size, 3)) {
            val (label, value) = entries[i]
            val pct = (value.toFloat() / total) * 100f
            tvs[i].text = "$label: ${"%.0f".format(pct)}%"
        }
        for (i in entries.size until 3) tvs[i].text = ""

        // Rank badge
        val rank = viewModel.getBusinessIdeaRank(idea)
        binding.numberButton.text = "#$rank"
    }

    // --------- Prompt ---------

    private fun buildPrompt(idea: BusinessIdea): String {
        val systemBlock = """
<Role>
You are an experienced business consultant and startup mentor with expertise across multiple industries.
Users gave their own scores for the different criteria in a questionnaire to test their business idea, but they might not be correct or understand the latest industry trends.
Your job is to verify their scores, and to analyze the business idea and provide clear, step-by-step guidance for aspiring entrepreneurs.
</Role>

<Instructions>
1. First, do an Idea Validation and Market Analysis in the form of a SWOT analysis.
2. Then, offer industry-specific recommendations.
3. Lastly, provide detailed action items.
4. Maintain an encouraging and supportive tone throughout.
</Instructions>

<Constraints>
- Avoid generic advice; always tailor guidance to the specific business idea.
- Focus on practical, actionable steps rather than theoretical concepts.
- Present information in digestible chunks.
- Include real-world examples when possible.
- Stay updated with current business trends and regulations.
</Constraints>
""".trimIndent()

        val contextBlock = """
<Context>
Idea name: ${idea.businessName}
Description: ${idea.businessDescription}

User scores (0–100):
- businessAnalysis: ${idea.businessAnalysis.toStableJson()}
- personalSkills: ${idea.personalSkills.toStableJson()}
- ownCriteria: ${idea.ownCriteria.toStableJson()}
</Context>
""".trimIndent()

        val task = """
<Task>
Using the above role and constraints, write:
• One concise paragraph (max 80 words) with smart, practical advice to improve or grow this idea, referencing market dynamics where relevant.
• Then three short bullet points with concrete next steps.
Return plain text only.
</Task>
""".trimIndent()

        return "$systemBlock\n\n$contextBlock\n\n$task"
    }

    // --------- Chart ---------

    private fun setupPieChart(pieChart: PieChart, tags: Map<String, Int>) {
        if (tags.isEmpty()) return
        val entries = tags.map { PieEntry(it.value.toFloat()) }
        val dataSet = PieDataSet(entries, "")

        val c = pieChart.context
        val colors = listOf(
            ContextCompat.getColor(c, R.color.offYellow),
            ContextCompat.getColor(c, R.color.tan),
            ContextCompat.getColor(c, R.color.paleOrange)
        )
        dataSet.colors = List(entries.size) { i -> colors[i % colors.size] }
        dataSet.sliceSpace = 2f
        dataSet.valueTextSize = 0f
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String = ""
        }

        pieChart.data = PieData(dataSet)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(false)
        pieChart.setDrawHoleEnabled(true)
        pieChart.holeRadius = 70f
        pieChart.transparentCircleRadius = 75f
        pieChart.invalidate()
    }

    // Sorted JSON for cleaner prompt
    private fun Map<String, Int>.toStableJson(): String =
        JSONObject(this.toSortedMap(compareBy { it.lowercase(Locale.getDefault()) }) as Map<*, *>).toString()

    companion object {
        private const val TAG = "BusinessPageActivity"
    }
}
