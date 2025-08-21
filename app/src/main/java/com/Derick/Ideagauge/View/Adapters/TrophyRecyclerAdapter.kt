package com.Derick.Ideagauge.View.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.View.Activities.BusinessPageActivity
import com.Derick.Ideagauge.databinding.TrophyCardRecBinding

class TrophyRecyclerAdapter(private var ideas: List<BusinessIdea>,private val context:Context) :
    RecyclerView.Adapter<TrophyRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: TrophyCardRecBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(idea: BusinessIdea) {
            binding.businessName.text = idea.businessName
            binding.businessDescription.text = idea.businessDescription
            binding.rankTxt.text = "#${adapterPosition + 1}"
            val card = binding.homeCardView

            card.setOnClickListener {
                val intent = Intent(context, BusinessPageActivity::class.java)
                intent.putExtra("idea_position", adapterPosition)
                intent.putExtra("DateId",idea.date.time)
                intent.putExtra("fragment", 2)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = TrophyCardRecBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        Log.d(TAG, "onBindViewHolder: position: $position")
        holder.bind(ideas[position])
    }

    override fun getItemCount(): Int = ideas.size

    fun updateList(newIdeas: List<BusinessIdea>) {
        ideas = newIdeas
        notifyDataSetChanged()
    }




    companion object{
        private const val TAG = "HomeRecAdapter"
    }
}
