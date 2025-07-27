package com.jamie.businessideasevaluator.View.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jamie.businessideasevaluator.View.Fragments.BusinessAnalysisFragment
import com.jamie.businessideasevaluator.View.Fragments.OwnCriteriaFragment
import com.jamie.businessideasevaluator.View.Fragments.PersonalSkillsFragment
import com.jamie.businessideasevaluator.View.Fragments.TutorialBusinessFragment
import com.jamie.businessideasevaluator.View.Fragments.TutorialPersonalFragment

class ExampleViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TutorialBusinessFragment()
            1 -> TutorialPersonalFragment()
            else -> throw IllegalStateException("Invalid step index")
        }
    }
}