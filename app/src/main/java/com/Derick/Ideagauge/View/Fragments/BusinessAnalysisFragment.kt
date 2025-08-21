package com.Derick.Ideagauge.View.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.Derick.Ideagauge.Data.SD.Questions
import com.Derick.Ideagauge.View.Activities.RankingsActivity
import com.Derick.Ideagauge.View.Adapters.SeekbarRecyclerAdapter
import com.Derick.Ideagauge.ViewModel.RankingViewModel
import com.Derick.Ideagauge.databinding.FragmentBusinessAnalysisBinding
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BusinessAnalysisFragment : Fragment() {
    private var _binding: FragmentBusinessAnalysisBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questions = Questions().businessAnalysisQuestions

        val adapter = SeekbarRecyclerAdapter(questions as MutableMap<String, List<String>>,0) { allAnswered, updatedMap ->

            if (allAnswered) {
                binding.saveCard.isEnabled = true
            }
            viewModel.updateBusinessAnalysis(updatedMap)
        }

        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

        binding.saveCard.setOnClickListener {
            (activity as? RankingsActivity)?.viewPager?.currentItem = 1

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "BusinessAnalysisFragmen"

    }
}