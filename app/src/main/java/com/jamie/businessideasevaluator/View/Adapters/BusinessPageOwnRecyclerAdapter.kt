package com.jamie.businessideasevaluator.View.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.R

class BusinessPageOwnRecyclerAdapter(
    private val idea: BusinessIdea
) : RecyclerView.Adapter<BusinessPageOwnRecyclerAdapter.ViewHolder>() {

    private val analysisList: List<Pair<String, Int>> = idea.ownCriteria.toList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyText: TextView = itemView.findViewById(R.id.question)
        val valueText: TextView = itemView.findViewById(R.id.answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.business_page_rec_card_own, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = analysisList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (key, value) = analysisList[position]
        holder.keyText.text = key
        if (value >= 50) {
            holder.valueText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        } else {
            holder.valueText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
        holder.valueText.text = value.toString()


    }
}