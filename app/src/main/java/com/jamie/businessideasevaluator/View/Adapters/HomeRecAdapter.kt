package com.jamie.businessideasevaluator.View.Adapters

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Activities.BusinessPageActivity
import com.jamie.businessideasevaluator.databinding.HomeBusinessCardBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HomeRecAdapter(private var ideas: List<BusinessIdea>,private val context:Context) :
    RecyclerView.Adapter<HomeRecAdapter.BusinessIdeaViewHolder>() {

    inner class BusinessIdeaViewHolder(private val binding: HomeBusinessCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(idea: BusinessIdea, position: Int, previousIdea: BusinessIdea?) {
            binding.businessName.text = idea.businessName
            binding.businessDescription.text = idea.businessDescription

            // Format and show/hide date
            val currentDate = idea.date
            val previousDate = previousIdea?.date

            val formattedDate = getFormattedDate(currentDate)

            if (previousDate != null && isSameDay(currentDate, previousDate)) {
                binding.date.visibility = View.GONE
            } else {
                binding.date.visibility = View.VISIBLE
                binding.date.text = formattedDate
            }

            // Pie Chart setup
            setupPieChart(binding.businessPieChart, idea.businessTags)

            // Tag percentage labels
            val tagEntries = idea.businessTags.entries.toList()
            val total = tagEntries.sumOf { it.value }

            val textViews = listOf(binding.textOne, binding.textTwo, binding.textThree)

            for (i in tagEntries.indices.take(3)) {
                val label = tagEntries[i].key
                val value = tagEntries[i].value.toFloat()
                val percent = (value / total) * 100
                textViews[i].text = "$label: ${"%.0f".format(percent)}%"
            }

            // Clear any unused text views
            for (i in tagEntries.size until 3) {
                textViews[i].text = ""
            }

            // Click handler
            binding.homeCardView.setOnClickListener {
                val intent = Intent(context, BusinessPageActivity::class.java)
                intent.putExtra("idea_position", adapterPosition)
                intent.putExtra("fragment", 1)
                context.startActivity(intent)
            }
        }
    }

    private fun getFormattedDate(date: Date): String {
        return if (DateUtils.isToday(date.time)) {
            "Today"
        } else {
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            formatter.format(date)
        }
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
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
        val idea = ideas[position]
        val previousIdea = if (position > 0) ideas[position - 1] else null
        holder.bind(idea, position, previousIdea)
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

