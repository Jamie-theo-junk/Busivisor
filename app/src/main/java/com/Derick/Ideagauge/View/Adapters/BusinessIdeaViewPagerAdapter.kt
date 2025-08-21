package com.Derick.Ideagauge.View.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.Derick.Ideagauge.View.Fragments.BusinessAnalysisFragment
import com.Derick.Ideagauge.View.Fragments.OwnCriteriaFragment
import com.Derick.Ideagauge.View.Fragments.PersonalSkillsFragment

class BusinessIdeaViewPagerAdapter(activity: AppCompatActivity,
    private val businessName: String?,
    private val businessDescription: String?
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusinessAnalysisFragment()
            1 -> PersonalSkillsFragment()
            2 -> OwnCriteriaFragment.newInstance(businessName, businessDescription)
            else -> throw IllegalStateException("Invalid step index")
        }
    }
}