package com.Derick.Ideagauge.View.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.Derick.Ideagauge.View.Fragments.TutorialBusinessFragment
import com.Derick.Ideagauge.View.Fragments.TutorialPersonalFragment

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