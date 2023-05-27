package chard.prj.smsapplication

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chard.prj.smsapplication.ui.viewmodel.MessageViewModel

class BaseViewModel (private val application: Application) : ViewModelProvider.Factory  {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MessageViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}