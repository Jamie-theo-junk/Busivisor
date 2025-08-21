package com.Derick.Ideagauge.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.Derick.Ideagauge.View.Adapters.SeekbarRecyclerAdapter
import com.Derick.Ideagauge.ViewModel.RankingViewModel
import com.Derick.Ideagauge.databinding.FragmentTutorialPersonalBinding
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