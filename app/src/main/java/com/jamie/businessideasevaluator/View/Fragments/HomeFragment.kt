package com.jamie.businessideasevaluator.View.Fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.Data.SD.Quotes
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.HomeRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.RecyclerItemDecoration
import com.jamie.businessideasevaluator.ViewModel.HomeViewModel
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
        val businessAdvice = Quotes().quotes.random()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        viewModel.initRepository(requireContext())

        val recyclerView = binding.businessIdeasRec

        val adapter = HomeRecAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         val bottomMarginPx = resources.getDimensionPixelSize(R.dimen.last_item_margin)
         recyclerView.addItemDecoration(RecyclerItemDecoration(bottomMarginPx))


        viewModel.ideas.observe(viewLifecycleOwner) { ideas ->
            adapter.updateList(ideas)
            setUpImage(ideas)
        }

        viewModel.loadIdeas()

    }
    private fun setUpImage(ideas: List<BusinessIdea>?) {
        val imageView = binding.girlImage // your ImageView ID from XML

        if (!ideas.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.girl_idea_png) // when list is not empty
            binding.dailyQuote.text = businessAdvice
        } else {
            imageView.setImageResource(R.drawable.girl_confused_png) // when list is empty or null
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}