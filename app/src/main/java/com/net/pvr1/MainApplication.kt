package com.net.pvr1

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.net.pvr1.ui.splash.SplashActivity
import com.net.pvr1.utils.sfmc.SFMCActionReceiver
import com.phonepe.intent.sdk.api.PhonePe
import com.salesforce.marketingcloud.MCLogListener
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.MarketingCloudSdk
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions
import com.salesforce.marketingcloud.notifications.NotificationManager
import com.salesforce.marketingcloud.notifications.NotificationMessage
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogLevel
import com.salesforce.marketingcloud.sfmcsdk.components.logging.LogListener
import dagger.hilt.android.HiltAndroidApp
import kotlin.random.Random

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PhonePe.init(this)

        FirebaseApp.initializeApp(this)

        // Loging SFMC
        if (BuildConfig.DEBUG) {
            SFMCSdk.setLogging(LogLevel.DEBUG, LogListener.AndroidLogger())
            MarketingCloudSdk.setLogLevel(MCLogListener.VERBOSE)
            MarketingCloudSdk.setLogListener(MCLogListener.AndroidLogListener())
        }

        // Configure SFMC

        SFMCSdk.configure(applicationContext as Application, SFMCSdkModuleConfig.build {
            pushModuleConfig = MarketingCloudConfig.builder().apply {
                setApplicationId("4898484d-1ad1-4fd8-b9cd-c5ae60f42bb4")
                setAccessToken("To5CnfaC88yKlV9CFi9hW1ES")
                setSenderId("305576178599")
                setMid("526001231")
                setMarketingCloudServerUrl("https://mcnd1yfl2dm3wzz62nbwyjffcvl0.device.marketingcloudapis.com/")
                setPiAnalyticsEnabled(true)
                setAnalyticsEnabled(true)
                setNotificationCustomizationOptions(
                    NotificationCustomizationOptions.create { context, notificationMessage ->
                        val builder = NotificationManager.getDefaultNotificationBuilder(
                            context,
                            notificationMessage,
                            NotificationManager.createDefaultNotificationChannel(context),
                            R.drawable.sm_push_logo
                        )
                        if (notificationMessage.customKeys != null && notificationMessage.customKeys.isNotEmpty()) {
                            showSFMCPushNotification(context, notificationMessage, builder)
                        }
                        builder.setContentIntent(
                            NotificationManager.redirectIntentForAnalytics(
                                context,
                                getIntentForPush(
                                    notificationMessage.url.toString(),
                                    context,
                                    Random.nextInt()
                                ),
                                notificationMessage,
                                true
                            )
                        )
                    }
                )
            }.build(applicationContext)
        }) {
            // TODO handle initialization status
        }
    }

    private fun getIntentForPush(url: String, context: Context, requestCode: Int): PendingIntent {
        val pendingIntent = if (url.isNullOrEmpty()) {
            PendingIntent.getActivity(
                context,
                requestCode,
                Intent(context, SplashActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                requestCode,
                Intent(Intent.ACTION_VIEW, Uri.parse(url)),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        return pendingIntent
    }

    @SuppressLint("NotificationTrampoline", "UnspecifiedImmutableFlag")
    private fun showSFMCPushNotification(
        context: Context,
        notificationMessage: NotificationMessage,
        builder: NotificationCompat.Builder
    ): NotificationCompat.Builder? {

        for (entry in notificationMessage.customKeys) {
            if (!entry.key.equals("category", ignoreCase = true)) {
                val notificationId = java.util.Random().nextInt() //new Random().nextInt();
                val abridged = notificationId % 1000
                Log.e("ASD", notificationId.toString())
                val intent = Intent(context, SFMCActionReceiver::class.java)
                intent.setPackage(context.packageName)
                intent.data = Uri.parse(entry.value)
                intent.putExtra("NT", notificationId)
                intent.putExtra("VALUE", entry.value)
                intent.putExtra("ASD", abridged)
                var pendingAction: PendingIntent? = null
                pendingAction =
                    PendingIntent.getBroadcast(
                        context,
                        notificationId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                val newIntent = NotificationManager.redirectIntentForAnalytics(
                    context,
                    pendingAction,
                    notificationMessage,
                    true
                )
                builder.addAction(0, entry.key, newIntent)
                builder.setContentIntent(newIntent)
            }
        }
        return builder
    }
}