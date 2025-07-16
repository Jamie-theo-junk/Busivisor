package com.jamie.businessideasevaluator.View.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.databinding.OwnCriteriaRecCardBinding
import com.jamie.businessideasevaluator.databinding.PersonalSkillRecCardBinding
import com.jamie.businessideasevaluator.databinding.QuestionRecCardBinding


class SeekbarRecyclerAdapter(
    private val questions: MutableMap<String, List<String>>,
    private val fragment: Int,
    private val onAllQuestionsAnswered: (Boolean, Map<String, Int>) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val answersMap = mutableMapOf<String, Int>()
    private var questionList = questions.keys.toList()

    init {
        for (question in questionList) {
            answersMap[question] = 0
        }
        onAllQuestionsAnswered(true, answersMap)
    }

    override fun getItemViewType(position: Int): Int {
        return fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ONE -> {
                val binding = QuestionRecCardBinding.inflate(inflater, parent, false)
                ViewHolderTypeOne(binding)
            }
            TYPE_TWO -> {
                val binding = PersonalSkillRecCardBinding.inflate(inflater, parent, false)
                ViewHolderTypeTwo(binding)
            }
            TYPE_THREE -> {
                val binding = OwnCriteriaRecCardBinding.inflate(inflater, parent, false)
                ViewHolderTypeThree(binding)
            }
            else -> throw IllegalArgumentException("Invalid fragment type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = questionList[position]
        val options = questions[question] ?: listOf("No", "Yes")

        when (holder) {
            is ViewHolderTypeOne -> holder.bind(question, options)
            is ViewHolderTypeTwo -> holder.bind(question, options)
            is ViewHolderTypeThree -> holder.bind(question, options)
        }
    }

    override fun getItemCount(): Int = questions.size

    inner class ViewHolderTypeOne(private val binding: QuestionRecCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String, options: List<String>) {
            binding.questionName.text = question
            binding.negativeOption.text = options[0]
            binding.positiveOption.text = options[1]
            binding.customSeekBar.progress = answersMap[question] ?: 0

            binding.customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        answersMap[question] = progress
                        onAllQuestionsAnswered(true, answersMap)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    inner class ViewHolderTypeTwo(private val binding: PersonalSkillRecCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String, options: List<String>) {
            binding.questionName.text = question
            binding.negativeOption.text = options[0]
            binding.positiveOption.text = options[1]
            binding.customSeekBar.progress = answersMap[question] ?: 0

            binding.customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        answersMap[question] = progress
                        onAllQuestionsAnswered(true, answersMap)
                    }

                }


                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    inner class ViewHolderTypeThree(private val binding: OwnCriteriaRecCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: String, options: List<String>) {
            binding.questionName.text = question
            binding.negativeOption.text = options[0]
            binding.positiveOption.text = options[1]
            binding.customSeekBar.progress = answersMap[question] ?: 0

            binding.customSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        answersMap[question] = progress
                        onAllQuestionsAnswered(true, answersMap)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    fun addQuestion(newQuestion: String) {
        questions[newQuestion] = listOf("No", "Yes")
        answersMap[newQuestion] = 0


        questionList = questions.keys.toList()
        notifyItemInserted(questionList.size - 1)
    }

    companion object {
        const val TYPE_ONE = 0
        const val TYPE_TWO = 1
        const val TYPE_THREE = 2
    }
}
