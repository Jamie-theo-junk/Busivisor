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
import com.Derick.Ideagauge.databinding.FragmentTutorialBusinessBinding
import kotlin.getValue


class TutorialBusinessFragment: Fragment() {
    private var _binding: FragmentTutorialBusinessBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()
    private lateinit var businessAnalysisQuestions:Map<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTutorialBusinessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpData()
        val questions = businessAnalysisQuestions
        val adapter = SeekbarRecyclerAdapter(questions as MutableMap<String, List<String>>,0) { allAnswered, updatedMap ->
            viewModel.updateBusinessAnalysis(updatedMap)
        }

        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

    }
    private fun setUpData(){
        businessAnalysisQuestions = mapOf(
            "Has it been done before?" to listOf("No", "Yes"),
            "Does it do things in a more valuable way?" to listOf("No", "Yes")
        )
    }

        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "BusinessAnalysisFragment"

    }
}