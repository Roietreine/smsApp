package chard.prj.smsapplication.ui

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import chard.prj.smsapplication.BaseViewModel
import chard.prj.smsapplication.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        messageList = ArrayList()
        displayMessage()
        setContentView(binding.root)
    }

    private fun displayMessage() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) if(
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_SMS),
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

    override fun onSenderClick(sender: String) {
        val intent = Intent(this, SenderActivity::class.java)
        intent.putExtra("sender", sender)
        startActivity(intent)

    }
}


