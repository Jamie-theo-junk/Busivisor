package com.jamie.businessideasevaluator.View.Adapters

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.OwnCriteriaRecCardBinding
import com.jamie.businessideasevaluator.databinding.PersonalSkillRecCardBinding
import com.jamie.businessideasevaluator.databinding.QuestionRecCardBinding


class SeekbarRecyclerAdapter(
    private val questions: MutableMap<String, List<String>>,
    private val fragment: Int,
    private val onAllQuestionsAnswered: (Boolean, Map<String, Int>) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val answersMap = mutableMapOf<String, Int>()
    private var questionList = questions.keys.toMutableList()

    init {
//        if(fragment!=TYPE_THREE) {
            for (question in questionList) {
                answersMap[question] = 0
            }
            onAllQuestionsAnswered(true, answersMap)
//        }
    }

    override fun getItemViewType(position: Int): Int = fragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ONE -> ViewHolderTypeOne(QuestionRecCardBinding.inflate(inflater, parent, false))
            TYPE_TWO -> ViewHolderTypeTwo(PersonalSkillRecCardBinding.inflate(inflater, parent, false))
            TYPE_THREE -> ViewHolderTypeThree(OwnCriteriaRecCardBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Invalid fragment type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = questionList[position]
        val options = questions[question] ?: listOf("No", "Yes")

        when (holder) {
            is ViewHolderTypeOne -> holder.bind(question, options, position)
            is ViewHolderTypeTwo -> holder.bind(question, options)
            is ViewHolderTypeThree -> holder.bind(question, options)
        }
    }

    override fun getItemCount(): Int = questionList.size

    inner class ViewHolderTypeOne(private val binding: QuestionRecCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String, options: List<String>, position: Int) {
            binding.questionName.text = "$question?"
            binding.negativeOption.text = options[0]
            binding.positiveOption.text = options[1]
            binding.customSeekBar.progress = answersMap[question] ?: 0

            binding.questionImage.visibility = if (position == 18) View.VISIBLE else View.GONE
            binding.questionImage.setOnClickListener {
                val context = itemView.context
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_question, null)
                val infoText = dialogView.findViewById<TextView>(R.id.info)
                infoText.text = "Blue Oceans are untapped market spaces with little or no competition. Businesses create new demand by offering unique value through innovation.\n\nRed Oceans are existing markets full of competitors. Companies fight over the same customers, leading to price wars and limited growth."

                AlertDialog.Builder(context)
                    .setTitle("Red Ocean and Blue Ocean?")
                    .setView(dialogView)
                    .setPositiveButton("Close") { d, _ -> d.dismiss() }
                    .create().show()
            }

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

    inner class ViewHolderTypeTwo(private val binding: PersonalSkillRecCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String, options: List<String>) {
            binding.questionName.text = "$question?"
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

    inner class ViewHolderTypeThree(private val binding: OwnCriteriaRecCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: String, options: List<String>) {
                    binding.questionName.text = "$question?"
                    binding.negativeOption.text = options[0]
                    binding.positiveOption.text = options[1]
                    binding.customSeekBar.progress = answersMap[question] ?: 0

            var currentQuestion = question

            binding.editImage.setOnClickListener {
                val context = itemView.context
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_criteria, null)
                val editText = dialogView.findViewById<EditText>(R.id.criteriaInput)
                editText.setText(currentQuestion)

                val dialog = AlertDialog.Builder(context)
                    .setTitle("Change the question")
                    .setView(dialogView)
                    .setPositiveButton("Save") { d, _ ->
                        val newQuestion = editText.text.toString().trim()
                        Log.d(TAG, "bind: newQuestion: $newQuestion")
                        if (newQuestion.isNotEmpty() && newQuestion != currentQuestion) {
                            val pos = adapterPosition
                            if (pos != RecyclerView.NO_POSITION) {

                                val value = questions.remove(currentQuestion)
                                val answer = answersMap.remove(currentQuestion)

                                Log.d(TAG, "bind: value: $value")
                                Log.d(TAG, "bind: answer: $answer")
                                for ((questionKey, answerValue) in answersMap) {

                                        Log.d(TAG, "Business Question: $questionKey | Answer Value: $answerValue")

                                }

                                    questions[newQuestion] = mutableListOf("No", "Yes")
                                    answersMap[newQuestion] = answer ?: 0
                                    questionList = questionList.toMutableList().apply {
                                        set(pos, newQuestion)
                                    }
                                onAllQuestionsAnswered(true, answersMap)
                                    notifyItemChanged(pos)
                            }
                        }
                        d.dismiss()
                    }
                    .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
                    .create()

                dialog.show()

            }

            binding.dustinImage.setOnClickListener {
                val context = itemView.context

                val alertDialog = AlertDialog.Builder(context)
                    .setTitle("Delete Criteria")
                    .setMessage("Are you sure you want to delete this criteria?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        val pos = adapterPosition
                        if (pos != RecyclerView.NO_POSITION) {
                            val questionToRemove = questionList[pos]
                            questions.remove(questionToRemove)
                            answersMap.remove(questionToRemove)
                            questionList.removeAt(pos)
                            notifyItemRemoved(pos)

                            onAllQuestionsAnswered(true, answersMap)
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()


                alertDialog.show()
            }

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
        if (!questions.containsKey(newQuestion)) {
            questions[newQuestion] = listOf("No", "Yes")
            answersMap[newQuestion] = 0
            questionList.add(newQuestion)
            notifyItemInserted(questionList.size - 1)
        }
    }

    companion object {
        const val TYPE_ONE = 0
        const val TYPE_TWO = 1
        const val TYPE_THREE = 2
        private const val TAG = "SeekbarRecyclerAdapter"
    }
}

