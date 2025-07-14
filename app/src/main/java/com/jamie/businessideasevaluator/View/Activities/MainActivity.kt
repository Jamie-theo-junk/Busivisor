package com.jamie.businessideasevaluator.View.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.jamie.businessideasevaluator.R
import com.jamie.businessideasevaluator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (!wasFabTutorialShown()) {
            showFabTutorial()
        }

        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = binding.cardBottomNav

        binding.iconHome.setOnClickListener {
            if (navController.currentDestination?.id != R.id.HomeFragment) {
                navController.navigate(R.id.HomeFragment)
            }
        }
        binding.iconTrophy.setOnClickListener {
            if (navController.currentDestination?.id != R.id.TrophyFragment) {
                navController.navigate(R.id.TrophyFragment)
            }
        }
        binding.fabAdd.setOnClickListener {
            markFabTutorialAsShown()
            val intent = Intent(this, BusinessNameActivity::class.java)
            startActivity(intent)
        }

    }

    private fun showFabTutorial() {
        TapTargetView.showFor(this,
            TapTarget.forView(
                binding.fabAdd,
                "Add a Business Idea",
                "Tap here to create your first idea!"
            )
                .outerCircleColor(R.color.background_grey)
                .targetCircleColor(R.color.white)
                .titleTextColor(R.color.white)
                .descriptionTextColor(R.color.white)
                .cancelable(false)
                .tintTarget(true)
                .transparentTarget(true)
                .targetRadius(60),
            object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view)
                    binding.fabAdd.performClick() // Trigger FAB manually
                }
            }
        )
    }

    private fun wasFabTutorialShown(): Boolean {
        val prefs = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("fab_tutorial_shown", false)
    }

    private fun markFabTutorialAsShown() {
        val prefs = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("fab_tutorial_shown", true).apply()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}