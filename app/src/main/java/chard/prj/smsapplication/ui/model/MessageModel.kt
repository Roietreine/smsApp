package chard.prj.smsapplication.ui.model

import android.os.Parcelable
import java.io.Serializable


data class MessageModel (
    val sender: String? = null,
    val body: String? = null,
    val timestamp: Long? = null
) : Serializable
