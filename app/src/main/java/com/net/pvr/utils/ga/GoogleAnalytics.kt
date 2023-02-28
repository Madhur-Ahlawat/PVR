package com.net.pvr.utils.ga

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class GoogleAnalytics {
    companion object{
        private var mFirebaseAnalytics: FirebaseAnalytics? = null

        fun hitEvent(context: Context, eventName: String, bundle: Bundle) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            mFirebaseAnalytics?.logEvent(eventName, bundle)

        }
    }

}