package com.herasymchuk.andrushchenko.apps.customview.fromscratch.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.herasymchuk.andrushchenko.R

class TicTacToeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.ticTackToeFieldStyle,
    defStyleRes: Int = R.style.DefaultTicTacToeFieldStyle
) : View(context, attrs, defStyleAttr, defStyleRes)
