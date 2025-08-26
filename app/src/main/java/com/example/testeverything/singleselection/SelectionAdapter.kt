package com.example.testeverything.singleselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testeverything.R
import com.example.testeverything.singleselection.SelectionAdapter.SelectionViewHolder
import com.example.testeverything.databinding.ItemSelectionBinding

class SelectionAdapter(private val onItemClick: (SelectableItem, Int) -> Unit) :
    ListAdapter<SelectableItem, SelectionViewHolder>(SelectionDiffCallback()) {

    class SelectionViewHolder(
        private val binding: ItemSelectionBinding,
        private val onItemClick: (SelectableItem, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        // ViewHolder implementation
        fun bind(item: SelectableItem, position: Int) {
            binding.apply {
                textTitle.text = item.title
                textDescription.text = item.description

                if (item.isSelected) {
                    llSelectionItem.setBackgroundResource(R.drawable.selected_item_background)

                } else {
                    llSelectionItem.setBackgroundResource(R.drawable.unselected_item_background)
                }

                root.setOnClickListener {
                    onItemClick(item, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectionViewHolder {
        val binding =
            ItemSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectionViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(
        holder: SelectionViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
    }

    class SelectionDiffCallback :
        ItemCallback<SelectableItem>() {
        override fun areItemsTheSame(oldItem: SelectableItem, newItem: SelectableItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SelectableItem, newItem: SelectableItem): Boolean {
            return oldItem == newItem
        }
    }
}