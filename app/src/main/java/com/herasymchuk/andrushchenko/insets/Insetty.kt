package com.herasymchuk.andrushchenko.insets

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.applyInsets(
    top: Boolean = false,
    left: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false,
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        if (top) v.updatePadding(top = insets.top)
        if (left) v.updatePadding(left = insets.left)
        if (right) v.updatePadding(right = insets.right)
        if (bottom) v.updatePadding(bottom = insets.bottom)
        windowInsets
    }
}
