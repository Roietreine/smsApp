package chard.prj.smsapplication.ui.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chard.prj.smsapplication.databinding.ItemMessageDetailsBinding
import chard.prj.smsapplication.ui.model.MessageModel
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(
    private var messageGroups: List<List<MessageModel>>,
    private var listener: SenderInterface
) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(val adapts: ItemMessageDetailsBinding) :
        RecyclerView.ViewHolder(adapts.root) {

        val senderDetails: TextView = adapts.tvSmsSender
        fun bind(messageDetails: List<MessageModel>) {

            senderDetails.text = messageDetails.firstOrNull()?.sender
            if (messageDetails.isNotEmpty()) {
                val firstMessage = messageDetails[0]
                adapts.tvSmsBody.text = firstMessage.body
                adapts.tvSmsBody.ellipsize = TextUtils.TruncateAt.MARQUEE
                adapts.tvSmsBody.isSelected = true
                adapts.root.setOnClickListener {
                    listener.onSenderClick(firstMessage.sender!!)
                }
                adapts.smsDate.text = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(firstMessage.timestamp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val messageItemView =
            ItemMessageDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(messageItemView)
    }

    override fun getItemCount(): Int {
        return messageGroups.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msgsGroup = messageGroups[position]
        holder.bind(msgsGroup)
    }

    fun updateList(newSmsList: List<List<MessageModel>>) {
        messageGroups = newSmsList
    }


}