package com.herasymchuk.andrushchenko.apps.customview.fromscratch

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.utils.withTypedArray

class TicTacToeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ticTackToeFieldStyle,
    defStyleRes: Int = R.style.DefaultTicTacToeFieldStyle,
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private val listener: OnFieldChangedListener = {

    }

    private var ticTacToeField: TicTacToeField = TicTacToeField.Base(0, 0)
        set(value) {
            field.unsubscribe(listener)
            field = value
            value.subscribe(listener)
            updateViewSizes()
            requestLayout()
            invalidate()
        }

    private var player1Color = Color.parseColor(PLAYER1_DEFAULT_COLOR)
    private var player2Color = Color.parseColor(PLAYER2_DEFAULT_COLOR)
    private var gridColor = Color.parseColor(GRID_DEFAULT_COLOR)

    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f

    init {
        if (attributeSet != null) {
            withTypedArray(attributeSet, R.styleable.TicTacToeView, defStyleAttr, defStyleRes) {
                player1Color = getColor(R.styleable.TicTacToeView_player1Color, player1Color)
                player2Color = getColor(R.styleable.TicTacToeView_player2Color, player2Color)
                gridColor = getColor(R.styleable.TicTacToeView_gridColor, gridColor)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ticTacToeField.subscribe(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ticTacToeField.unsubscribe(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom
        val desiredCellSizePx: Int = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_CELL_SIZE_DP, resources.displayMetrics
        ).toInt()
        val rows = ticTacToeField.rows
        val columns = ticTacToeField.columns

        val desiredWidth = maxOf(minWidth, columns * desiredCellSizePx + paddingLeft + paddingRight)
        val desiredHeight = maxOf(minHeight, rows * desiredCellSizePx + paddingTop + paddingBottom)
        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSizes()
    }

    private fun updateViewSizes() {
        if (ticTacToeField.rows == 0 || ticTacToeField.columns == 0) return
        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom
        val cellWidth = safeWidth / ticTacToeField.columns.toFloat()
        val cellHeight = safeHeight / ticTacToeField.rows.toFloat()
        cellSize = minOf(cellHeight, cellWidth)
        cellPadding = cellSize * 0.2f

        val fieldWidth = cellSize * ticTacToeField.columns
        val fieldHeight = cellSize * ticTacToeField.rows

        fieldRect.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = "#008800"
        const val PLAYER2_DEFAULT_COLOR = "#880000"
        const val GRID_DEFAULT_COLOR = "#dfdfdf"
        const val DESIRED_CELL_SIZE_DP = 50f
    }
}
