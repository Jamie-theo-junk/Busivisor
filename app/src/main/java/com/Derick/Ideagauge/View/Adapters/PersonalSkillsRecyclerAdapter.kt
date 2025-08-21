package com.Derick.Ideagauge.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.Derick.Ideagauge.databinding.PersonalSkillRecCardBinding

class PersonalSkillsRecyclerAdapter (
    private val questions: List<String>,
    private val onProgressChanged: (Map<String, Int>) -> Unit
) : RecyclerView.Adapter<PersonalSkillsRecyclerAdapter.SeekBarViewHolder>() {

    private val answersMap = mutableMapOf<String, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeekBarViewHolder {
        val binding = PersonalSkillRecCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeekBarViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SeekBarViewHolder,
        position: Int
    ) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    inner class SeekBarViewHolder(private val binding: PersonalSkillRecCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String) {
            binding.questionName.text = "$question?"


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