package chard.prj.smsapplication.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import chard.prj.smsapplication.ui.model.MessageModel

class MessageViewModel(application: Application) : AndroidViewModel(application) {

    private val _smsItems = MutableLiveData<List<MessageModel>>()
    val smsItems: LiveData<List<MessageModel>> get() = _smsItems

    init {
        val observer = SmsHelper(this)
        getApplication<Application>().contentResolver.registerContentObserver(
            Telephony.Sms.CONTENT_URI,
            true,
            observer
        )
        loadSms()
    }

    @SuppressLint("Recycle")
    fun loadSms() {
        val cursor = getApplication<Application>().contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val smsList = mutableListOf<MessageModel>()
        cursor?.use {
            while (it.moveToNext()) {
                val sender = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = it.getString(it.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(Telephony.Sms.DATE))
                val smsMessage = MessageModel(sender, body, timestamp)
                smsList.add(smsMessage)
            }
        }
        cursor?.close()
        _smsItems.postValue(smsList)
    }



    fun addMessage(message: MessageModel) {
        _smsItems.value = _smsItems.value.orEmpty().plus(message)
        // Get the latest message from the list

    }


}