package com.jamie.businessideasevaluator.View.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.Data.SD.Questions
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Activities.CompletedTickActivity
import com.jamie.businessideasevaluator.View.Adapters.OwnCriteriaRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.PersonalSkillsRecyclerAdapter
import com.jamie.businessideasevaluator.ViewModel.HomeViewModel
import com.jamie.businessideasevaluator.ViewModel.RankingViewModel
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding
import com.jamie.businessideasevaluator.databinding.FragmentOwnCriteriaBinding
import com.jamie.businessideasevaluator.databinding.FragmentPersonalSkillsBinding
import com.jamie.businessideasevaluator.databinding.OwnCriteriaRecCardBinding
import kotlin.getValue

class OwnCriteriaFragment : Fragment() {

    private var _binding: FragmentOwnCriteriaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOwnCriteriaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initRepository(this.requireContext())
        val questions = Questions().ownCriteria
        val adapter = OwnCriteriaRecAdapter(questions) { updatedMap ->
            viewModel.updateOwnCriteria(updatedMap)
        }

        binding.seekBarRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.seekBarRecyclerView.adapter = adapter

        binding.saveCard.setOnClickListener {
            val name = "temporary"
            val description = "temporary"

            if (name.isNotEmpty() && description.isNotEmpty()) {
                viewModel.finalizeAndInsertIdea(name, description)

                val toCompleted = Intent(this.requireContext(), CompletedTickActivity::class.java)
                startActivity(toCompleted)
            }
        }

    }
}