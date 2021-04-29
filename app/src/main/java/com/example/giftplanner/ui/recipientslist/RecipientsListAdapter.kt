package com.example.giftplanner.ui.recipientslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.databinding.RecipientsListFragmentBinding
import com.example.giftplanner.databinding.RecipientsListItemFragmentBinding
import kotlinx.android.synthetic.main.recipients_list_item_fragment.view.*

class RecipientsListAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Recipient, RecipientsListAdapter.ViewHolder>(DiffCallback())  {

    private var list: List<Recipient> = ArrayList()

    fun setData(list: List<Recipient>) {
        this.list = list
        submitList(this.list)
    }

    interface OnItemClickListener{
        fun onItemClick(recipient: Recipient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecipientsListItemFragmentBinding.inflate(
            LayoutInflater.from(parent.context)
            , parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: RecipientsListItemFragmentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(list[position])
                    }
                }
            }
        }
        fun bind(recipient: Recipient) = with(itemView) {
            binding.apply {
                nameView.text = recipient.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipient>() {
        override fun areItemsTheSame(oldItem: Recipient, newItem: Recipient) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipient, newItem: Recipient) =
            oldItem == newItem
    }
}