package chard.prj.smsapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import chard.prj.smsapplication.BaseViewModel
import chard.prj.smsapplication.databinding.ActivitySenderBinding
import chard.prj.smsapplication.ui.adapter.SenderAdapter
import chard.prj.smsapplication.ui.viewmodel.MessageViewModel

class SenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySenderBinding
    private val viewModel: MessageViewModel by viewModels {
        BaseViewModel(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayAllMessage()
        backButton()

    }

    private fun backButton() {
        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun displayAllMessage() {
        val sender = intent.getStringExtra("sender")
        viewModel.smsItems.observe(this) { smsList ->
            val messageGroup = smsList.filter { it.sender == sender }.groupBy { it.body }.values.toList()
            val adapts = SenderAdapter(messageGroup)
            binding.senderMessages.apply {
                layoutManager = LinearLayoutManager(this@SenderActivity)
                setHasFixedSize(true)
                adapter = adapts

            }
        }
    }
}