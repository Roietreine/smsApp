package chard.prj.smsapplication.utils

import chard.prj.smsapplication.ui.model.MessageModel

interface MessageObserver {
    fun onMessageReceived(message: MessageModel)
}