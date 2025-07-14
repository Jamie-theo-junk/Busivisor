package com.jamie.businessideasevaluator.View.Fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.Data.SD.Qoutes
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.HomeRecAdapter
import com.jamie.businessideasevaluator.View.Adapters.RecyclerItemDecoration
import com.jamie.businessideasevaluator.ViewModel.HomeViewModel
import com.jamie.businessideasevaluator.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
        val businessAdvice = Qoutes().quotes.random()
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
         val loadingOverlay = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
         loadingOverlay.visibility = View.VISIBLE
        val adapter = HomeRecAdapter(emptyList(),this.requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         val bottomMarginPx = resources.getDimensionPixelSize(R.dimen.last_item_margin)
         recyclerView.addItemDecoration(RecyclerItemDecoration(bottomMarginPx))


         viewModel.ideas.observe(viewLifecycleOwner) { ideas ->
             lifecycleScope.launch {
                 val startTime = System.currentTimeMillis()

                 adapter.updateList(ideas)
                 setUpImage(ideas)

                 val elapsedTime = System.currentTimeMillis() - startTime
                 val remainingTime = 500 - elapsedTime

                 if (remainingTime > 0) {
                     delay(remainingTime)
                 }

                 loadingOverlay.visibility = View.GONE
             }
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