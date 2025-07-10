package com.jamie.businessideasevaluator.View.Adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.HomeBusinessCardBinding


class HomeRecAdapter(private var ideas: List<BusinessIdea>) :
    RecyclerView.Adapter<HomeRecAdapter.BusinessIdeaViewHolder>() {

    inner class BusinessIdeaViewHolder(private val binding: HomeBusinessCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(idea: BusinessIdea) {
            binding.businessName.text = idea.businessName
            binding.businessDescription.text = idea.businessDescription
            setupPieChart(binding.businessPieChart, idea.businessTags)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BusinessIdeaViewHolder {
        val binding =
            HomeBusinessCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BusinessIdeaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BusinessIdeaViewHolder,
        position: Int
    ) {
        Log.d(TAG, "onBindViewHolder: position: $position")
        holder.bind(ideas[position])
    }

    override fun getItemCount(): Int = ideas.size

    fun updateList(newIdeas: List<BusinessIdea>) {
        ideas = newIdeas
        notifyDataSetChanged()
    }

    private fun setupPieChart(pieChart: PieChart, tags: Map<String, Int>) {
        if (tags.isEmpty()) return

        val entries = tags.map { PieEntry(it.value.toFloat()) }

        val dataSet = PieDataSet(entries, "")

        // Get colors from context
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


    companion object{
        private const val TAG = "HomeRecAdapter"
    }
}

