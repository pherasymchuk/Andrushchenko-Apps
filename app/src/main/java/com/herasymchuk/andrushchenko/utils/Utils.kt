package com.herasymchuk.andrushchenko.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes
import androidx.core.content.res.ResourcesCompat
import kotlin.reflect.KClass

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

fun <T : Activity> Context.startApp(appClass: KClass<T>) {
    startActivity(Intent(this, appClass.java))
}
