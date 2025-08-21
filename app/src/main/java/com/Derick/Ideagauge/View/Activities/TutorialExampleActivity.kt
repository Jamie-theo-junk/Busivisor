package com.Derick.Ideagauge.View.Activities

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.Derick.Ideagauge.R
import com.Derick.Ideagauge.View.Adapters.ExampleViewPagerAdapter
import com.Derick.Ideagauge.databinding.ActivityTutorialExampleBinding

class TutorialExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialExampleBinding
    private var whereWeAt = "Okay"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTutorialExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.okayBtn.setOnClickListener {
            when (whereWeAt) {
                "Okay" -> {
                    setUpViewpager()
                    whereWeAt = "Next"
                }
                "Next" -> {
                    toNextPager()
                    binding.textview.text = "Finish"
                    whereWeAt = "Finish"
                }
                "Finish" -> {
                    startActivity(Intent(this, TutorialCompleted::class.java))

                }
            }
        }

    }


    private fun setUpViewpager() {
        val textView = binding.textView
        val promptTextVIew = binding.promptTextTwo
        val description = binding.businessDescriptionInput
        val viewpager = binding.exampleViewpager
        val relative = binding.topAppLogo


        viewpager.adapter = ExampleViewPagerAdapter(this)


        description.animate()
            .translationY(-description.height.toFloat())
            .alpha(0f)
            .setDuration(400)
            .withEndAction {

                description.visibility = View.GONE
                description.translationY = 0f
                description.alpha = 1f


                val layoutParams = textView.layoutParams as ViewGroup.MarginLayoutParams
                val fromMarginTop = layoutParams.topMargin
                val toMarginTop =
                    (30 * textView.resources.displayMetrics.density).toInt()


                ValueAnimator.ofInt(fromMarginTop, toMarginTop).apply {
                    duration = 300
                    addUpdateListener { animator ->
                        layoutParams.topMargin = animator.animatedValue as Int
                        textView.layoutParams = layoutParams
                    }
                    start()
                }

                val relativeLayoutParams = relative.layoutParams as ViewGroup.MarginLayoutParams
                val fromRelativeMarginTop = relativeLayoutParams.topMargin
                val toRelativeMarginTop = (40 * relative.resources.displayMetrics.density).toInt()

                ValueAnimator.ofInt(fromRelativeMarginTop, toRelativeMarginTop).apply {
                    duration = 300
                    addUpdateListener { animator ->
                        relativeLayoutParams.topMargin = animator.animatedValue as Int
                        relative.layoutParams = relativeLayoutParams
                    }
                    start()
                }


                textView.animate()
                    .translationYBy(-10f)
                    .setDuration(300)
                    .start()


                viewpager.alpha = 0f
                viewpager.translationY = 50f
                promptTextVIew.visibility = View.VISIBLE
                viewpager.visibility = View.VISIBLE
                binding.textview.text = "Next"

                viewpager.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .start()
            }
            .start()
    }

    private fun toNextPager() {
        val viewpager = binding.exampleViewpager
        val currentItem = viewpager.currentItem
        val itemCount = viewpager.adapter?.itemCount ?: 0


        if (currentItem < itemCount - 1) {
            viewpager.setCurrentItem(currentItem + 1, true)
        }
    }
}