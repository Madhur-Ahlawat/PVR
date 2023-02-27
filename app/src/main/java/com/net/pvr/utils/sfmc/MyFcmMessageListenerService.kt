package com.net.pvr.utils.sfmc

import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.salesforce.marketingcloud.messages.push.PushMessageManager
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk

public class MyFcmMessageListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            val extras = Bundle()
           for (entry in message.data){
               extras.putString(entry.key,entry.value.toString())
           }
            if (PushMessageManager.isMarketingCloudPush(message))
            {
                SFMCSdk.requestSdk { sdk ->
                    sdk.mp {
                        it.pushMessageManager.handleMessage(message)
                    }
                }

            }
        }
    }

    override fun onNewToken(token: String) {
        SFMCSdk.requestSdk { sdk ->
            sdk.mp {
                it.pushMessageManager.setPushToken(token)
            }
        }
    }
}