package com.sesac.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun Lifecycle.EffectPauseStop(onEvent: () -> Unit) {
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE ||
                event == Lifecycle.Event.ON_STOP
            ) {
                onEvent()
            }
        }

        this@EffectPauseStop.addObserver(observer)

        onDispose {
            this@EffectPauseStop.removeObserver(observer)
        }
    }
}