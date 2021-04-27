package com.example.giftplanner.ui.presentslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.databinding.PresentsListFragmentBinding
import kotlinx.android.synthetic.main.presents_list_item_fragment.view.*

class PresentsListAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Present, PresentsListAdapter.ViewHolder>(DiffCallback()) {

    private var list: List<Present> = ArrayList()

    fun setData(list: List<Present>) {
        this.list = list
        submitList(this.list)
    }

    interface OnItemClickListener{
        fun onItemClick(present: Present)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PresentsListFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: PresentsListFragmentBinding)
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
        fun bind(present: Present) = with(itemView) {
            binding.apply {
                nameView.text = present.name
                costView.text = present.cost.toString()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Present>() {
        override fun areItemsTheSame(oldItem: Present, newItem: Present) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Present, newItem: Present) =
            oldItem == newItem
    }
}