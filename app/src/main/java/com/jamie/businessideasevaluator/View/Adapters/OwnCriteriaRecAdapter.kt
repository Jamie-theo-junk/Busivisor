package com.jamie.businessideasevaluator.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.View.Adapters.OwnCriteriaRecAdapter.OwnCriteriaViewHolder
import com.jamie.businessideasevaluator.databinding.OwnCriteriaRecCardBinding
import com.jamie.businessideasevaluator.databinding.PersonalSkillRecCardBinding

class OwnCriteriaRecAdapter (
    private val questions: List<String>,
    private val onProgressChanged: (Map<String, Int>) -> Unit
) : RecyclerView.Adapter<OwnCriteriaViewHolder>() {

    private val answersMap = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnCriteriaViewHolder {
        val binding = OwnCriteriaRecCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OwnCriteriaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OwnCriteriaViewHolder,
        position: Int
    ) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    inner class OwnCriteriaViewHolder(private val binding: OwnCriteriaRecCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String) {
            binding.questionName.text = question

            // Initialize with default or existing value
            binding.customSeekBar.progress = answersMap[question] ?: 50

            binding.customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    answersMap[question] = progress
                    onProgressChanged(answersMap)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        }
    }
}