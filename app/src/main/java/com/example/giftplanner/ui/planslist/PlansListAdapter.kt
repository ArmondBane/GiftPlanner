package com.example.giftplanner.ui.planslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.databinding.PlansListFragmentBinding
import com.example.giftplanner.databinding.PlansListItemFragmentBinding
import kotlinx.android.synthetic.main.plans_list_item_fragment.view.*

class PlansListAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Plan, PlansListAdapter.ViewHolder>(DiffCallback()) {

    private var list: List<Plan> = ArrayList()

    fun setData(list: List<Plan>) {
        this.list = list
        submitList(this.list)
    }

    interface OnItemClickListener{
        fun onItemClick(plan: Plan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlansListItemFragmentBinding.inflate(
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

    inner class ViewHolder(private val binding: PlansListItemFragmentBinding)
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
        fun bind(plan: Plan) = with(itemView) {
            binding.apply {
                holidayNameView.text = plan.holidayName
                dateView.text = plan.dateFormatted
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan) =
            oldItem == newItem
    }
}