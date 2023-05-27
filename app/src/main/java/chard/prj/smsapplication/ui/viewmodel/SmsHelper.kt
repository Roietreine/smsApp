package chard.prj.smsapplication.ui.viewmodel

import android.database.ContentObserver

class SmsHelper(private val viewModel: MessageViewModel) : ContentObserver(null) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        viewModel.loadSms()
    }
}