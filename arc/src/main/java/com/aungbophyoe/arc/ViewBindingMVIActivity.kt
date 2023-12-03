package com.aungbophyoe.arc

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.ViewDataBinding
import com.kaopiz.kprogresshud.KProgressHUD
import org.orbitmvi.orbit.viewmodel.observe

abstract class ViewBindingMVIActivity<V : ViewDataBinding, VM : MVIViewModel<VS, SE>, VS : Any, SE : Any> : ViewBindingActivity<V>() {
    abstract val viewModel: VM
    private lateinit var dialog: KProgressHUD
    abstract fun handleSideEffect(sideEffect: SE)
    abstract fun render(state: VS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initProgressDialog()
        viewModel.observe(
            lifecycleOwner = this,
            sideEffect = ::handleSideEffect,
            state = ::render
        )
    }

    @Throws(Exception::class)
    fun initProgressDialog() {
        dialog = KProgressHUD.create(this)
            .setWindowColor(resources.getColor(R.color.purple_200))
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setAnimationSpeed(1)
            .setDimAmount(0.3f)
    }

    @Throws(Exception::class)
    fun showProgressDialog() {
        synchronized(this) {
            if (!dialog.isShowing) {
                dialog.show()
            }
        }
    }


    @Throws(Exception::class)
    fun dismissProgressDialog() {
        synchronized(this) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }
}