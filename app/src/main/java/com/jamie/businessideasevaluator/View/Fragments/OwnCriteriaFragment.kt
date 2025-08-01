package com.jamie.businessideasevaluator.View.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.Data.SD.Questions
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Activities.CompletedTickActivity
import com.jamie.businessideasevaluator.View.Adapters.OwnCriteriaRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.SeekbarRecyclerAdapter
import com.jamie.businessideasevaluator.ViewModel.RankingViewModel
import com.jamie.businessideasevaluator.databinding.FragmentOwnCriteriaBinding
import kotlin.getValue

class OwnCriteriaFragment : Fragment() {

    private var _binding: FragmentOwnCriteriaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        _binding = FragmentOwnCriteriaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initRepository(this.requireContext())
        val questions = Questions().ownCriteria

        val adapter = SeekbarRecyclerAdapter(questions as MutableMap<String, List<String>>,2) { allAnswered, updatedMap ->

            if (allAnswered) {
                binding.saveCard.isEnabled = true
            }
            viewModel.updateOwnCriteria(updatedMap as MutableMap<String, Int>)
        }
        binding.addCriteriaCard.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_criteria, null)
            val inputField = dialogView.findViewById<EditText>(R.id.criteriaInput)

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add Custom Criteria")
                .setView(dialogView)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create()

            dialog.setOnShowListener {
                val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                positive.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_orange))
                negative.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))

                positive.setOnClickListener {
                    val newCriteria = inputField.text.toString()
                    if (newCriteria.isNotBlank()) {
                        adapter.addQuestion(newCriteria)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Please enter a valid criteria", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            dialog.show()
        }
        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

        binding.saveCard.setOnClickListener {
            val businessName = arguments?.getString("business_name")
            val businessDescription = arguments?.getString("business_description")

            if (businessName!!.isNotEmpty() && businessDescription!!.isNotEmpty()) {
                viewModel.finalizeAndInsertIdea(businessName, businessDescription)
                val toCompleted = Intent(this.requireContext(), CompletedTickActivity::class.java)
                startActivity(toCompleted)
            }
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(businessName: String?, businessDescription: String?): OwnCriteriaFragment {
            val fragment = OwnCriteriaFragment()
            val args = Bundle().apply {
                putString("business_name", businessName)
                putString("business_description", businessDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }
}