package com.herasymchuk.andrushchenko.apps.customview.fromscratch

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.herasymchuk.andrushchenko.R

class TicTacToeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ticTackToeFieldStyle,
    defStyleRes: Int = R.style.DefaultTicTacToeFieldStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var player1Color: Int = Color.parseColor(PLAYER1_DEFAULT_COLOR)
    private var player2Color: Int = Color.parseColor(PLAYER2_DEFAULT_COLOR)
    private var gridColor: Int = Color.parseColor(GRID_DEFAULT_COLOR)

    init {
        if (attrs != null) {
            initAttrs(attrs, defStyleAttr, defStyleRes)
        }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.TicTacToeView,
            defStyleAttr,
            defStyleRes
        )
        player1Color = typedArray.getColor(R.styleable.TicTacToeView_player1Color, player1Color)
        player2Color = typedArray.getColor(R.styleable.TicTacToeView_player2Color, player2Color)
        gridColor = typedArray.getColor(R.styleable.TicTacToeView_gridColor, gridColor)
        typedArray.recycle()
    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = "#008800"
        const val PLAYER2_DEFAULT_COLOR = "#880000"
        const val GRID_DEFAULT_COLOR = "#dfdfdf"
    }
}
