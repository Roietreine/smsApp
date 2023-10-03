package chard.prj.smsapplication.service

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log


class SmsReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
                val bundle = intent.extras
                if (bundle != null) {
                    val pdus = bundle.get("pdus") as Array<*>
                    val messages = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in pdus.indices) {
                        messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    for (message in messages) {
                        val sender = message?.originatingAddress ?: ""
                        val body = message?.messageBody ?: ""
                        val timestamp = message?.timestampMillis ?: System.currentTimeMillis()


                        // Do something with the SMS message here
                        Log.d("SmsReceiver", "Received message from $sender: $body $timestamp")
                    }
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur while processing the SMS message
            Log.e("SmsReceiver", "Exception while processing SMS message", e)
        }
    }
}

