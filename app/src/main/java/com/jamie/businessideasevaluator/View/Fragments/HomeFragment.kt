package com.jamie.businessideasevaluator.View.Fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
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

    private lateinit var adapter: HomeRecAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initRepository(requireContext())

        val recyclerView = binding.businessIdeasRec
        val loadingOverlay = view.findViewById<RelativeLayout>(R.id.loadingOverlay)
        loadingOverlay.visibility = View.VISIBLE


        adapter = HomeRecAdapter(mutableListOf(), requireContext()) { ideaToRemove ->
            lifecycleScope.launch {
                val success = viewModel.removeIdea(ideaToRemove)
                if (success) {
                    val index = adapter.getIdeaPosition(ideaToRemove)
                    if (index != -1) {
                        adapter.removeConfirmed(index)
                    }
                    Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to remove item", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.businessIdeasRec.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val bottomMarginPx = resources.getDimensionPixelSize(R.dimen.last_item_margin)
        recyclerView.addItemDecoration(RecyclerItemDecoration(bottomMarginPx))

        // Setup swipe to remove
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val paint = Paint()
                val icon = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_delete)
                val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2

                if (dX < 0) {
                    paint.color = Color.RED
                    c.drawRect(
                        itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                    )

                    val iconTop = itemView.top + iconMargin
                    val iconLeft = itemView.right - iconMargin - (icon?.intrinsicWidth ?: 0)
                    val iconRight = itemView.right - iconMargin
                    val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)
                    icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon?.draw(c)

                    val textPaint = Paint().apply {
                        color = Color.WHITE
                        textSize = 40f
                        typeface = Typeface.DEFAULT_BOLD
                        isAntiAlias = true
                    }
                    val text = "Remove from list"
                    val textWidth = textPaint.measureText(text)
                    val textX = iconLeft - textWidth - 20
                    val textY = itemView.top + (itemView.height / 2) + (textPaint.textSize / 2) - 10
                    c.drawText(text, textX, textY, textPaint)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

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
        val imageView = binding.girlImage

        if (!ideas.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.girl_image_regular)
            binding.dailyQuote.text = businessAdvice
        } else {
            imageView.setImageResource(R.drawable.girl_confused)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
