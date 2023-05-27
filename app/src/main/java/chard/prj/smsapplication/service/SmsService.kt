package chard.prj.smsapplication.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat

//class SmsService  : Service(){
//
//    lateinit var activeNotificationReceiver: ActiveNotificationReceiver
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.RECEIVE_SMS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Register the SmsNotificationReceiver if permission is granted
//
//            activeNotificationReceiver = ActiveNotificationReceiver()
//            val intentFilter = IntentFilter()
//            intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
//            registerReceiver(activeNotificationReceiver, intentFilter)
//        } else {
//            Log.d("SmsService", "RECEIVE_SMS permission not granted")
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(SmsReceiver())
//        unregisterReceiver(activeNotificationReceiver)
//    }
//}