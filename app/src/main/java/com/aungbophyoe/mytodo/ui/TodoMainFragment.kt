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

           /* val nums = intArrayOf(1,1,0,1,1,1)
            val result = findMaxSequenceNumber(nums)
            Log.d("result test 1","$result")

            val numsTwo = intArrayOf(1,0,1,1,0,1)
            val resultTwo = findMaxSequenceNumber(numsTwo)
            Log.d("result test 2","$resultTwo")

            val numTest1 = intArrayOf(2,7,11,15)
            val targetTest1 = 9
            val resultTest1 = addTwoSum(numTest1,targetTest1)
            Log.d("result numTest1","${resultTest1.contentToString()}")

            val numTest2 = intArrayOf(3,2,4)
            val targetTest2 = 6
            val resultTest2 = addTwoSum(numTest2,targetTest2)
            Log.d("result numTest2","${resultTest2.contentToString()}")

            val numTest3 = intArrayOf(3,3)
            val targetTest3 = 6
            val resultTest3 = addTwoSum(numTest3,targetTest3)
            Log.d("result numTest3","${resultTest3.contentToString()}")*/
//0,1


            // code test

            /*val input1 = "Apple"
            val input2 = "VTech"
            val input3 = "Flipping Case"
            val input4 = "Year 2023"

            val result1 = changeCharacter(input1)
            Log.d("result 1", "$result1")
            val result2 = changeCharacter(input2)
            Log.d("result 2", "$result2")
            val result3 = changeCharacter(input3)
            Log.d("result 3", "$result3")
            val result4 = changeCharacter(input4)
            Log.d("result 4", "$result4")*/

          /*  Test#1
            Input: “Apple”
            Output: “aPPLE”
            Test#2
            Input: “VTech”
            Output: “vtECH”

            Test#3
            Input: “Flipping Case”
            Output: “fLIPPING cASE”

            Test#4
            Input: “Year 2023”
            Output: “yEAR 2023”*/


            /*
            *
            * Test #1
Input: s = "anagram", t = "nagaram"
Output: true

Test#2
Input: s = "anagrama", t = "nagaram"
Output: false

Test #3
Input: s = "rat", t = "car"
Output: false*/

            val test1 = isAnagram(s = "anagram",t = "nagaram")
            Log.d("Test 1","$test1")
            val test2 = isAnagram(s = "anagrama",t = "nagaram")
            Log.d("Test 2","$test2")
            val test3 = isAnagram(s = "rat",t = "car")
            Log.d("Test 3","$test3")



        }
    }

    private fun isAnagram(s:String,t:String) : Boolean {
        if(s.length != t.length) {
            return false
        }
        val charCount = mutableMapOf<Char,Int>()
        //Input: s = "anagram", t = "nagaram"
        // a - 1

        for (ch in s) {
            charCount[ch] = charCount.getOrDefault(ch,0) + 1
        }
        for (ch in t) {
            val count = charCount.getOrDefault(ch,0)
            if(count == 0) {
                return false
            }
            charCount[ch] = count - 1
        }
        return true
    }


    private fun changeCharacter(input : String) : String {
       return input.map {
            when {
                it.isUpperCase() -> it.lowercase()
                it.isLowerCase() -> it.uppercase()
                else -> it
            }
        }.joinToString("")
    }

    private fun addTwoSum(nums: IntArray,target : Int) : IntArray {
        val numIndicesMap = mutableMapOf<Int,Int>()
        for(i in nums.indices){
            val dd = target - nums[i]
            if(numIndicesMap.containsKey(dd)) {
                return intArrayOf(numIndicesMap[dd]!!,i)
            }
            numIndicesMap[nums[i]] = i
        }
        return intArrayOf(-1,-1)
    }

    private fun findMaxSequenceNumber(nums : IntArray) : Int {
        var result = 0
        var current = 0
        for (num in nums) {
            if(num == 1){
                current++
                result = maxOf(result,current)
            } else {
                current = 0
            }
        }
        return result
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
        viewModel.updateTodo(todo = item,position,isCheck = true)
    }
}