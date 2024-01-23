package com.herasymchuk.andrushchenko.utils

import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.ResourcesCompat

inline fun View.withTypedArray(
    set: AttributeSet?,
    @StyleableRes attrs: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    action: TypedArray.() -> Unit,
) {
    val typedArray = context.theme.obtainStyledAttributes(
        set, attrs, defStyleAttr, defStyleRes
    )
    action(typedArray)
    typedArray.recycle()
}

@ColorInt
fun View.resolveColor(@ColorRes colorRes: Int): Int =
    ResourcesCompat.getColor(this.context.resources, colorRes, null)
