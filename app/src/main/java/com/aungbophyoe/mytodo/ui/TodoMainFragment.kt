package com.aungbophyoe.mytodo.ui

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aungbophyoe.arc.ViewBindingMVIFragment
import com.aungbophyoe.arc.utility.hide
import com.aungbophyoe.arc.utility.hideSoftKeyboard
import com.aungbophyoe.arc.utility.setSafeClickListener
import com.aungbophyoe.arc.utility.show
import com.aungbophyoe.arc.utility.textChanges
import com.aungbophyoe.domain.model.Todo
import com.aungbophyoe.mytodo.R
import com.aungbophyoe.mytodo.databinding.FragmentTodoMainBinding
import com.aungbophyoe.mytodo.databinding.TodoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TodoMainFragment : ViewBindingMVIFragment<FragmentTodoMainBinding, TodoViewModel, TodoState, TodoSideEffect>(),TodoAdapter.ItemOnClickListener,TodoAdapter.CheckOnClickListener {
    override val layoutRes: Int = R.layout.fragment_todo_main

    override val viewModel: TodoViewModel by viewModels()

    private val todoBottomSheet by lazy {
        BottomSheetDialog(requireContext(),R.style.BottomSheetTheme)
    }

    private val todoAdapter by lazy {
        TodoAdapter(this,this)
    }

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
                    sideEffect.position.let {
                        if(it<todoAdapter.itemCount) {
                            todoAdapter.notifyItemChanged(it)
                        }
                    }
                }
            }
        }
    }

    override fun render(state: TodoState) {
        viewBinding.apply {
            Log.d("render state","${state.type}")
            state.data.let {
                todoAdapter.submitList(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            favCreateTodo.setSafeClickListener {
                showTodoBottomSheet()
            }
            rvMyTodos.apply {
                layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                adapter = todoAdapter
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

            filterRadioGroup.setOnCheckedChangeListener { group, checkedId ->
               /* when (checkedId) {
                    R.id.rbAll -> {
                        viewModel.setFilterState(FilterType.ALL)
                        viewModel.fetchAllTodos()
                    }

                    R.id.rbCompleted -> {
                        viewModel.setFilterState(FilterType.COMPLETED)
                        viewModel.fetchAllTodos()
                    }

                    R.id.rbUncompleted -> {
                        viewModel.setFilterState(FilterType.UNCOMPLETED)
                        viewModel.fetchAllTodos()
                    }
                }*/
            }
        }
    }

    private fun showTodoBottomSheet(isUpdate : Boolean = false,todo: Todo? = null,position: Int? = null) {
        val binding = TodoBottomSheetBinding.inflate(layoutInflater) // Replace with your binding class
        binding.apply {
            if(isUpdate) {
                tvTitle.text = "Update Todo Task"
                btnOk.text = "Update"
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
                        viewModel.updateTodo(todo,position!!)
                    }
                } else {
                    viewModel.addTodo(text)
                }
                activity?.hideSoftKeyboard()
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
                val todo = todoAdapter.currentList.getOrNull(position)
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

    override fun itemOnClick(item: Todo, position: Int) {
        showTodoBottomSheet(isUpdate = true,item,position)
    }

    override fun checkOnClick(item: Todo, position: Int) {
        viewModel.updateTodo(todo = item,position)
    }
}