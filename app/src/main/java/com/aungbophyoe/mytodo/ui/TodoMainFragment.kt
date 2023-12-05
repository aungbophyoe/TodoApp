package com.aungbophyoe.mytodo.ui

import android.graphics.Canvas
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungbophyoe.arc.ViewBindingMVIFragment
import com.aungbophyoe.arc.utility.hide
import com.aungbophyoe.arc.utility.setSafeClickListener
import com.aungbophyoe.arc.utility.show
import com.aungbophyoe.arc.utility.textChanges
import com.aungbophyoe.domain.model.Todo
import com.aungbophyoe.mytodo.R
import com.aungbophyoe.mytodo.databinding.FragmentTodoMainBinding
import com.aungbophyoe.mytodo.databinding.RowTodoItemBinding
import com.aungbophyoe.mytodo.databinding.TodoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TodoMainFragment : ViewBindingMVIFragment<FragmentTodoMainBinding, TodoViewModel, TodoState, TodoSideEffect>() {
    override val layoutRes: Int = R.layout.fragment_todo_main

    override val viewModel: TodoViewModel by viewModels()

    private val todoBottomSheet by lazy {
        BottomSheetDialog(requireContext(),R.style.BottomSheetTheme)
    }

    private val todoDelegate = AsyncListDifferDelegationAdapter(
        DiffUtils,
        todoItem (
            itemOnClick = {
                showTodoBottomSheet(isUpdate = true,it)
            },
            checkOnClick = {
                viewModel.updateTodo(todo = it)
            }
        )
    )

    override fun handleSideEffect(sideEffect: TodoSideEffect) {
        viewBinding.apply {
            when(sideEffect){
                is TodoSideEffect.Loading -> {
                    progressBar.show()
                    tvNoResult.hide()
                }
                is TodoSideEffect.ShowError -> {
                    progressBar.hide()
                    tvNoResult.hide()
                    Toast.makeText(requireContext(),"${sideEffect.message}",Toast.LENGTH_SHORT).show()
                }
                is TodoSideEffect.Success -> {
                    progressBar.hide()
                    tvNoResult.hide()
                    rvMyTodos.show()
                }
                is TodoSideEffect.Empty -> {
                    progressBar.hide()
                    tvNoResult.show()
                    rvMyTodos.hide()
                }

                is TodoSideEffect.UpdateSuccess -> {
                    progressBar.hide()
                    tvNoResult.hide()
                    todoDelegate.notifyDataSetChanged()
                }
            }
        }
    }

    override fun render(state: TodoState) {
        viewBinding.apply {
            state.data.let {
                todoDelegate.items = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            ivCreateNote.setSafeClickListener {
                showTodoBottomSheet()
            }
            rvMyTodos.apply {
                layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                adapter = todoDelegate
                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(rvMyTodos)
            }
            edtSearchBox.textChanges()
                .debounce(300L)
                .onEach {
                    if(it?.isNotEmpty() == true) {
                        viewModel.searchTodo(it.toString())
                    } else {
                        viewModel.fetchAllTodos()
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun showTodoBottomSheet(isUpdate : Boolean = false,todo: Todo? = null) {
        val binding = TodoBottomSheetBinding.inflate(layoutInflater) // Replace with your binding class
        binding.apply {
            if(isUpdate) {
                tvTitle.text = "Update Todo Task"
                todo?.let {
                    edtNewTodo.setText("${it.title}")
                }
            }
            edtNewTodo.textChanges()
                .debounce(300L)
                .onEach {
                    btnOk.isEnabled = it?.isNotEmpty() == true
                }
                .launchIn(lifecycleScope)
            btnOk.setSafeClickListener {
                val text = edtNewTodo.text.toString()
                if(text.isNullOrEmpty()){
                    edtNewTodo.error = "Required!"
                    return@setSafeClickListener
                }
                if (isUpdate) {
                    todo?.let {
                        todo.title = text
                        viewModel.updateTodo(todo)
                    }
                } else {
                    viewModel.addTodo(text)
                }
                todoBottomSheet.dismiss()
            }
        }
        todoBottomSheet.setContentView(binding.root)
        todoBottomSheet.setCanceledOnTouchOutside(false)
        todoBottomSheet.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        // fixed UI to bottom sheet
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        todoBottomSheet.show()
    }

    private fun todoItem(
        itemOnClick: (Todo) -> Unit,
        checkOnClick: (Todo) -> Unit
    ) = adapterDelegateViewBinding<Todo,Todo, RowTodoItemBinding>(
        { inflater, root ->
            DataBindingUtil.inflate(
                inflater,
                R.layout.row_todo_item,
                root,
                false
            )
        }
    ) {
        bind {
            binding.apply {
                item.let {data ->
                    model = data
                    root.setSafeClickListener {
                        itemOnClick(data)
                    }
                    checkbox.setSafeClickListener {
                        data.isCompleted = checkbox.isChecked
                        checkOnClick(data)
                    }
                }
            }
        }
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT
    ) {

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val swipeFlags = ItemTouchHelper.START
            return makeMovementFlags(0, swipeFlags)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val deleteIcon = ContextCompat.getDrawable(requireContext(),R.drawable.iv_delete)
            val background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_swipe_delete_red)
            background!!.setBounds(
                viewHolder.itemView.right + dX.toInt(),
                viewHolder.itemView.top,
                viewHolder.itemView.right,
                viewHolder.itemView.bottom
            )
            background.draw(c)

            // Draw the delete icon
            val iconMargin = (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2
            val iconLeft = viewHolder.itemView.right - iconMargin - deleteIcon.intrinsicWidth
            val iconTop = viewHolder.itemView.top + (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2
            val iconRight = viewHolder.itemView.right - iconMargin
            val iconBottom = iconTop + deleteIcon.intrinsicHeight

            deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            // Implement this method if needed
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val todo = todoDelegate.items.getOrNull(position)
                todo?.let {
                    viewModel.deleteTodo(todo)
                }
            }
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