package com.jamie.businessideasevaluator.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.Data.SD.Questions
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Activities.RankingsActivity
import com.jamie.businessideasevaluator.View.Adapters.SeekbarRecyclerAdapter
import com.jamie.businessideasevaluator.ViewModel.RankingViewModel
import com.jamie.businessideasevaluator.databinding.FragmentPersonalSkillsBinding
import com.jamie.businessideasevaluator.databinding.FragmentTutorialPersonalBinding
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.getValue

class TutorialPersonalFragment: Fragment() {
    private var _binding: FragmentTutorialPersonalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()
    private lateinit var personalSkillsQuestions:Map<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTutorialPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setUpData(){
        personalSkillsQuestions= mapOf(
            "Does it apply effectuation:" to listOf("No", "Yes"),
            "Does it fit my personality:" to listOf("No", "Yes"),
            "Am I interested in this:" to listOf("No", "Yes"))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        val questions = personalSkillsQuestions
        val adapter = SeekbarRecyclerAdapter(questions as MutableMap<String, List<String>>,1) { allAnswered, updatedMap ->


            viewModel.updatePersonalSkills(updatedMap)
        }

        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PersonalSkillsFragment"
    }
}