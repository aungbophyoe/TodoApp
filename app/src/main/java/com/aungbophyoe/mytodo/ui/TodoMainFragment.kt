package com.aungbophyoe.mytodo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import com.aungbophyoe.arc.ViewBindingMVIFragment
import com.aungbophyoe.domain.model.Todo
import com.aungbophyoe.mytodo.R
import com.aungbophyoe.mytodo.databinding.FragmentTodoMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoMainFragment : ViewBindingMVIFragment<FragmentTodoMainBinding, TodoViewModel, TodoState, TodoSideEffect>() {
    override val layoutRes: Int = R.layout.fragment_todo_main

    override val viewModel: TodoViewModel by viewModels()

    override fun handleSideEffect(sideEffect: TodoSideEffect) {
        viewBinding.apply {
            when(sideEffect){
                TodoSideEffect.Loading -> {

                }
                is TodoSideEffect.ShowError -> {

                }
            }
        }
    }

    override fun render(state: TodoState) {
        viewBinding.apply {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {

        }
    }

    object DiffUtils : DiffUtil.ItemCallback<Todo>(){
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }
}