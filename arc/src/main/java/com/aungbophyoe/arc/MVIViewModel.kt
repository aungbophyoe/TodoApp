package com.aungbophyoe.arc

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost

abstract class MVIViewModel<VS : Any, SE : Any> : ContainerHost<VS, SE>, ViewModel() {
}