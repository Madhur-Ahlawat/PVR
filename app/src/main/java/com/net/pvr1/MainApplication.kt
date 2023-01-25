package com.net.pvr1

import android.app.Application
import com.phonepe.intent.sdk.api.PhonePe
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        PhonePe.init(this)

    }
}