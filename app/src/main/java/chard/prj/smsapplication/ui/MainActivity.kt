package chard.prj.smsapplication.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import chard.prj.smsapplication.BaseViewModel
import chard.prj.smsapplication.databinding.ActivityMainBinding
import chard.prj.smsapplication.service.NotificationService
import chard.prj.smsapplication.ui.adapter.MessageAdapter
import chard.prj.smsapplication.ui.adapter.SenderInterface
import chard.prj.smsapplication.ui.model.MessageModel
import chard.prj.smsapplication.ui.viewmodel.MessageViewModel
import chard.prj.smsapplication.utils.Constant.REQUEST_SMS_PERMISSION

class MainActivity : AppCompatActivity()
    ,SenderInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var messageList: ArrayList<MessageModel>
    private val messageViewModel: MessageViewModel by viewModels {
        BaseViewModel(application)
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        messageList = ArrayList()
        displayMessage()
        notificationManager()
        setContentView(binding.root)
    }

    private fun displayMessage() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS),
                REQUEST_SMS_PERMISSION
            )
            return
        }
        messageViewModel.smsItems.observe(this) { smsList ->
            val messageGroup = smsList.groupBy { it.sender }.values.toList()
            val adapts = MessageAdapter(messageGroup,this)
            binding.messagesList.apply {
                adapts.updateList(messageGroup)
                layoutManager = LinearLayoutManager(this@MainActivity)
                setHasFixedSize(true)
                adapter = adapts
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, read SMS messages
                displayMessage()

            } else {
                // Permission denied, show a message to the user
                Toast.makeText(
                    this,
                    "Permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun notificationManager(){
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!notificationManager.isNotificationListenerAccessGranted(
                ComponentName(
                    this, NotificationService::class.java
                )
            )
        ) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)

            startService(Intent(this, NotificationService::class.java))
        }
}

    override fun onSenderClick(sender: String) {
        val intent = Intent(this, SenderActivity::class.java)
        intent.putExtra("sender", sender)
        startActivity(intent)

    }
}


