package com.jamie.businessideasevaluator.View.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.View.Adapters.BusinessIdeaViewPagerAdapter
import com.jamie.businessideasevaluator.databinding.ActivityRankingsBinding

class RankingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingsBinding
    lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRankingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val businessName: String? = intent.getStringExtra("business_name")
        val businessDescription: String? = intent.getStringExtra("business_description")
        val businessNameTextView = binding.BusinessName
        businessNameTextView.text = businessName
        val adapter = BusinessIdeaViewPagerAdapter(this, businessName, businessDescription)
        binding.viewPager.adapter = adapter


        viewPager = binding.viewPager
        viewPager.isUserInputEnabled = false

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateProgressIndicators(position)
            }
        })

    }
    private fun updateProgressIndicators(position: Int) {
        val indicators = listOf(
            binding.progressStep1,
            binding.progressStep2,
            binding.progressStep3
        )

        indicators.forEachIndexed { index, view ->
            view.setBackgroundResource(
                if (index <= position) R.drawable.progress_dash_active
                else R.drawable.progress_dash_inactive
            )
        }
    }
}