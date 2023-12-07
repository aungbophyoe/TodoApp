package com.aungbophyoe.mytodo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aungbophyoe.arc.utility.setSafeClickListener
import com.aungbophyoe.domain.model.Todo
import com.aungbophyoe.mytodo.databinding.RowTodoItemBinding

class TodoAdapter constructor(private val itemOnClickListener: ItemOnClickListener,private val checkOnClickListener: CheckOnClickListener) : ListAdapter<Todo,TodoAdapter.ViewHolder>(TodoMainFragment.DiffUtils) {
    interface ItemOnClickListener {
        fun itemOnClick(item : Todo,position:Int)
    }
    interface CheckOnClickListener {
        fun checkOnClick(item: Todo,position:Int)
    }
    inner class ViewHolder(private val binding: RowTodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Todo) {
            binding.apply {
                model = item
                mcvTodo.isEnabled = item.isCompleted.not()
                checkbox.isEnabled = item.isCompleted.not()
                mcvTodo.setSafeClickListener {
                    itemOnClickListener.itemOnClick(item,adapterPosition)
                }
                checkbox.setSafeClickListener {
                    item.isCompleted = checkbox.isChecked
                    checkOnClickListener.checkOnClick(item,adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowTodoItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }
}