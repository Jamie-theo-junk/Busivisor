package com.jamie.businessideasevaluator.View.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.Data.SD.Questions
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Activities.RankingsActivity
import com.jamie.businessideasevaluator.View.Adapters.PersonalSkillsRecyclerAdapter
import com.jamie.businessideasevaluator.View.Adapters.SeekbarRecyclerAdapter

import com.jamie.businessideasevaluator.ViewModel.RankingViewModel
import com.jamie.businessideasevaluator.databinding.FragmentBusinessAnalysisBinding
import com.jamie.businessideasevaluator.databinding.FragmentPersonalSkillsBinding
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalSkillsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonalSkillsFragment : Fragment() {
    private var _binding: FragmentPersonalSkillsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalSkillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val questions = Questions().personalSkillsQuestions
        val adapter = PersonalSkillsRecyclerAdapter(questions) { updatedMap ->
            viewModel.updatePersonalSkills(updatedMap)
        }

        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

        binding.nextCard.setOnClickListener {

            Log.d(TAG, "onViewCreated: ---------------------------")
            viewModel.businessAnalysis.forEach { (key, value) ->
                Log.d(TAG, "Personal Skills BusinessAnalysis -> $key: $value")
            }
            (activity as? RankingsActivity)?.viewPager?.currentItem = 2
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PersonalSkillsFragment"
    }
}