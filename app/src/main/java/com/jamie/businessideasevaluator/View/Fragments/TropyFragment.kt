package com.jamie.businessideasevaluator.View.Fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.HomeRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.RecyclerItemDecoration
import com.jamie.businessideasevaluator.View.Adapters.TrophyRecyclerAdapter
import com.jamie.businessideasevaluator.ViewModel.TropyViewModel
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding
import com.jamie.businessideasevaluator.databinding.FragmentTropyBinding

class TropyFragment : Fragment() {

    private var _binding: FragmentTropyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TropyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTropyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initRepository(requireContext())
        viewModel.loadRankedIdeas()


        val sortOptions = listOf("Overall", "Business Analysis", "Personal Skills", "Own Criteria")
        val spAdapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_custom_background,
            sortOptions
        ).apply {
            setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        }

        binding.sortSpinner?.adapter = spAdapter

        val adapter = TrophyRecyclerAdapter(emptyList(), requireContext())
        val recyclerView = binding.trophyRec
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bottomMarginPx = resources.getDimensionPixelSize(R.dimen.last_item_margin)
        recyclerView.addItemDecoration(RecyclerItemDecoration(bottomMarginPx))


        viewModel.ideas.observe(viewLifecycleOwner) { ideas ->
            if (!ideas.isNullOrEmpty()) {
                val bestIdea = ideas[0]
                binding.dailyQuote.text = "Looks like your best idea was \"${bestIdea.businessName}\""
            } else {
                adapter.updateList(emptyList())
                binding.dailyQuote.text = "No ideas yet. Time to brainstorm!"
            }
        }

        binding.sortSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedOption = parent?.getItemAtPosition(position).toString()
                val ideas = viewModel.ideas.value
                if (!ideas.isNullOrEmpty()) {
                    val sortedIdeas = sortIdeas(ideas, selectedOption)
                    adapter.updateList(sortedIdeas)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun sortIdeas(
        ideas: List<BusinessIdea>,
        selectedOption: String
    ): List<BusinessIdea> {
        return when (selectedOption) {
            "Business Analysis" -> ideas.sortedByDescending { averageScore(it.businessAnalysis) }
            "Personal Skills" -> ideas.sortedByDescending { averageScore(it.personalSkills) }
            "Own Criteria" -> ideas.sortedByDescending { averageScore(it.ownCriteria) }
            else -> {
                // Default "Overall" - average all criteria
                ideas.sortedByDescending {
                    val totalScores = listOf(
                        averageScore(it.businessAnalysis),
                        averageScore(it.personalSkills),
                        averageScore(it.ownCriteria)
                    )
                    totalScores.average()
                }
            }
        }
    }

    private fun averageScore(criteriaMap: Map<String, Int>): Double {
        return if (criteriaMap.isNotEmpty()) {
            criteriaMap.values.average()
        } else {
            0.0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}