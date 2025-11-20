package com.sesac.common.component


import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.MutableState


val LocalIsSearchOpen = compositionLocalOf<MutableState<Boolean>> {
    mutableStateOf(false)
}