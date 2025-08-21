package com.Derick.Ideagauge.View.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.Derick.Ideagauge.Data.SD.SharedPreferenceManager
import com.Derick.Ideagauge.R
import com.Derick.Ideagauge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: SharedPreferenceManager
    private var hasCheckedAd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        val prefs = getSharedPreferences("ad_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("ad_shown", false).apply()
        prefManager = SharedPreferenceManager(this)

//        var randomNumber = (1..100).random()
        if (!prefManager.hasCompletedTutorial()) {
            startActivity(Intent(this, TutorialStartUpActivity::class.java))
            finish()
            return
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun adInitial(){
         lateinit var mInterstitialAd: InterstitialAd
         var isAdLoaded = false

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    mInterstitialAd = ad
                    isAdLoaded = true
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("AdMob", "Ad failed to load: ${adError.message}")
                    isAdLoaded = false
                }
            })
        if (isAdLoaded) {
            mInterstitialAd.show(this)
        }

    }

    private fun showFabTutorial() {
        TapTargetView.showFor(
            this,
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
                    binding.fabAdd.performClick()
                }
            }
        )
    }

    private var backPressedTime: Long = 0

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finishAffinity()
        } else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
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

    companion object {
        private const val TAG = "MainActivity2.0"
    }

    override fun onResume() {
        super.onResume()


        val prefs = getSharedPreferences("ad_prefs", Context.MODE_PRIVATE)
        val adShown = prefs.getBoolean("ad_shown", false)

        Log.d(TAG, "onResume: adShown = $adShown")

        if (!adShown && prefManager.hasCompletedTutorial()) {
            val chance = (1..100).random()
            Log.d(TAG, "Chance value: $chance")

            if (chance <= 50) {
                Log.d(TAG, "onResume: launching BookAdActivity")
                prefs.edit().putBoolean("ad_shown", true).apply()
                startActivity(Intent(this, BookAdActivity::class.java))
            } else {
                Log.d(TAG, "onResume: ad not launched this time")
                prefs.edit().putBoolean("ad_shown", true)
                    .apply() // Still mark it to avoid rerunning

                adInitial()
            }
        }
    }
}