package chard.prj.smsapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import chard.prj.smsapplication.databinding.ItemConversationDetailsBinding
import chard.prj.smsapplication.ui.model.MessageModel
import java.text.SimpleDateFormat
import java.util.*


class SenderAdapter(private var messageGroups: List<List<MessageModel>>) :
    RecyclerView.Adapter<SenderAdapter.ViewHolder>() {


    inner class ViewHolder(val adapts: ItemConversationDetailsBinding) :
        RecyclerView.ViewHolder(adapts.root){

        val messageDetails: TextView = adapts.tvSmsBody
        fun bind(showAllMessage: List<MessageModel>) {
            messageDetails.text = showAllMessage.firstOrNull()?.body
            if (showAllMessage.isNotEmpty()) {
                val firstMessage = showAllMessage[0]
                adapts.tvSmsBody.text = firstMessage.body
                adapts.smsDate.text = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(firstMessage.timestamp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val senderList = ItemConversationDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(senderList)
    }

    override fun getItemCount(): Int {
        return messageGroups.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msgsGroup = messageGroups[position]
        holder.bind(msgsGroup)
            }

}