package com.yogaprasetyo.juaraandroid.guessgender.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yogaprasetyo.juaraandroid.guessgender.R
import com.yogaprasetyo.juaraandroid.guessgender.data.local.History
import com.yogaprasetyo.juaraandroid.guessgender.databinding.ItemHistoryBinding

class HistoryAdapter(val onItemClicked: (History) -> Unit) :
    ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    /**
     * Binding the data to the UI item
     * */
    class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: History?) {
            item?.let {
                binding.apply {
                    tvHistoryDate.text = it.date
                    tvHistoryName.text =
                        itemView.resources.getString(R.string.item_history, it.name)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)

        holder.bind(history)
        holder.binding.ivHistoryDelete.setOnClickListener { onItemClicked(history) } // Triggered icon trash
    }

    /**
     * Anonymous class for compare the oldList and newList in ListAdapter
     * */
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}