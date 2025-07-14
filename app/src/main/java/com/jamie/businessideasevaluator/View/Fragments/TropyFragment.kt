package com.jamie.businessideasevaluator.View.Fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.HomeRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.RecyclerItemDecoration
import com.jamie.businessideasevaluator.View.Adapters.TrophyRecyclerAdapter
import com.jamie.businessideasevaluator.ViewModel.TropyViewModel
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding
import com.jamie.businessideasevaluator.databinding.FragmentTropyBinding

class TropyFragment : Fragment() {

    companion object {
        fun newInstance() = TropyFragment()
    }

    private var _binding: FragmentTropyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TropyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTropyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.trophyRec
        viewModel.initRepository(this.requireContext())

        val adapter = TrophyRecyclerAdapter(emptyList(),this.requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val bottomMarginPx = resources.getDimensionPixelSize(R.dimen.last_item_margin)
        recyclerView.addItemDecoration(RecyclerItemDecoration(bottomMarginPx))

        viewModel.ideas.observe(viewLifecycleOwner) { ideas ->
            adapter.updateList(ideas)
        }
        viewModel.loadRankedIdeas()
    }
}