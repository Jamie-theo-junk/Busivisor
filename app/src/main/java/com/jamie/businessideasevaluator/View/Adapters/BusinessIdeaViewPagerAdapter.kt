package com.jamie.businessideasevaluator.View.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jamie.businessideasevaluator.View.Fragments.BusinessAnalysisFragment
import com.jamie.businessideasevaluator.View.Fragments.OwnCriteriaFragment
import com.jamie.businessideasevaluator.View.Fragments.PersonalSkillsFragment

class BusinessIdeaViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusinessAnalysisFragment()
            1 -> PersonalSkillsFragment()
            2 -> OwnCriteriaFragment()
            else -> throw IllegalStateException("Invalid step index")
        }
    }
}