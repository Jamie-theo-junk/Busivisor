package com.jamie.businessideasevaluator.View.Adapters

import android.app.Application
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(
            this
        ) { }
        appOpenManager = AppOpenManager(this)
    }

    companion object {
        private var appOpenManager: AppOpenManager? = null
    }
}