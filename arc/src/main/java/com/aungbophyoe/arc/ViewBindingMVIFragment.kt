package com.aungbophyoe.arc

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import org.orbitmvi.orbit.viewmodel.observe

abstract class ViewBindingMVIFragment<V : ViewDataBinding, VM : MVIViewModel<VS, SE>, VS : Any, SE : Any> :
    ViewBindingFragment<V>() {
    abstract val viewModel: VM
    private lateinit var dialog: KProgressHUD
    abstract fun handleSideEffect(sideEffect: SE)
    abstract fun render(state: VS)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        viewModel.observe(
            lifecycleOwner = this,
            sideEffect = ::handleSideEffect,
            state = ::render
        )
    }


    @Throws(Exception::class)
    fun initProgressDialog() {
        dialog = KProgressHUD.create(requireActivity())
            .setWindowColor(resources.getColor(R.color.purple_200))
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.3f)
    }
    @Throws(Exception::class)
    fun showProgressDialog() {
        synchronized(requireContext()) {
            if (!dialog.isShowing) {
                dialog.show()
            }
        }
    }

    @Throws(Exception::class)
    fun dismissProgressDialog() {
        synchronized(requireContext()) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }


}