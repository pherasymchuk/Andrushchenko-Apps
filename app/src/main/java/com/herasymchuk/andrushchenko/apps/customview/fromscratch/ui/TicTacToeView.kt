package com.herasymchuk.andrushchenko.apps.customview.fromscratch.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.view.updatePadding
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame.Cell
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame.FieldObserver
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame.TicTacToe
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.ui.TicTacToeView.OnCellClickedListener
import com.herasymchuk.andrushchenko.utils.withTypedArray

class TicTacToeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.ticTacToeFieldStyle,
    defStyleRes: Int = R.style.DefaultTicTacToeFieldStyle
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var ticTacToe: TicTacToe = TicTacToe.Base(3, 3)
        set(value) {
            field.unsubscribe(onFieldChangeListener)
            field = value
            field.subscribe(onFieldChangeListener)
            updateViewSizes()
            requestLayout()
            invalidate()
        }
    private var player1Color = Color.parseColor(PLAYER1_DEFAULT_COLOR)
        set(value) {
            field = value
            player1Paint.color = field
        }
    private var player2Color = Color.parseColor(PLAYER2_DEFAULT_COLOR)
        set(value) {
            field = value
            player2Paint.color = field
        }
    private var gridColor = Color.parseColor(GRID_DEFAULT_COLOR)
        set(value) {
            field = value
            gridPaint.color = field
        }
    private val fieldRectF: RectF = RectF()
    private val cellRectF: RectF = RectF()
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f
    private var currentRow: Int = -1
        set(value) {
            field = value
            invalidate()
        }
    private var currentColumn: Int = -1
        set(value) {
            field = value
            invalidate()
        }
    private val onFieldChangeListener = FieldObserver { ticTacToe = it }
    var onCellClickedListener = OnCellClickedListener { _, _, _ -> }
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gridColor
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            resources.displayMetrics
        )
    }
    private val player1Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = player1Color
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
    }
    private val player2Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = player2Color
        style = Paint.Style.STROKE
        strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            resources.displayMetrics
        )
    }

    init {
        if (attrs != null) {
            initializeAttributes(attrs, defStyleAttr, defStyleRes)
        }
        if (isInEditMode) {
            ticTacToe.setCell(ticTacToe.Position(2, 1), Cell.USER_2)
            ticTacToe.setCell(ticTacToe.Position(2, 2), Cell.USER_1)
            ticTacToe.setCell(ticTacToe.Position(1, 1), Cell.USER_1)
        }
        if (paddingLeft == 0) updatePadding(
            left = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3f,
                resources.displayMetrics
            ).toInt()
        )
        if (paddingRight == 0) updatePadding(
            right = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3f,
                resources.displayMetrics
            ).toInt()
        )
    }

    private fun initializeAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        withTypedArray(attrs, R.styleable.TicTacToeView, defStyleAttr, defStyleRes) {
            player1Color = getColor(R.styleable.TicTacToeView_player1Color, player1Color)
            player2Color = getColor(R.styleable.TicTacToeView_player2Color, player2Color)
            gridColor = getColor(R.styleable.TicTacToeView_gridColor, gridColor)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ticTacToe.subscribe(onFieldChangeListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ticTacToe.unsubscribe(onFieldChangeListener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight: Int = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellSizeInPixel = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DESIRED_CELL_SIZE,
            context.resources.displayMetrics
        ).toInt()

        val desiredWidthInPixels =
            maxOf(desiredCellSizeInPixel * ticTacToe.columns + paddingLeft + paddingRight, minWidth)
        val desiredHeightInPixels =
            maxOf(desiredCellSizeInPixel * ticTacToe.rows + paddingTop + paddingBottom, minHeight)

        val width = resolveSize(desiredWidthInPixels, widthMeasureSpec)
        val height = resolveSize(desiredHeightInPixels, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSizes()
    }

    private fun updateViewSizes() {
        val safeWidth: Int = width - paddingLeft - paddingRight
        val safeHeight: Int = height - paddingTop - paddingBottom

        cellSize = minOf(safeHeight / ticTacToe.rows.toFloat(), safeWidth / ticTacToe.columns.toFloat())
        cellPadding = cellSize * 0.2f

        val fieldWidth: Float = cellSize * ticTacToe.columns
        val fieldHeight: Float = cellSize * ticTacToe.rows

        fieldRectF.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRectF.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRectF.right = fieldRectF.left + fieldWidth
        fieldRectF.bottom = fieldRectF.top + fieldHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (cellSize <= 0f) return
        if (fieldRectF.width() <= 0f) return
        if (fieldRectF.height() <= 0f) return
        drawGrid(canvas)
        drawCells(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        for (row in 0..ticTacToe.rows) {
            val y: Float = fieldRectF.top + cellSize * row
            canvas.drawLine(fieldRectF.left, y, fieldRectF.right, y, gridPaint)
        }
        for (column in 0..ticTacToe.columns) {
            val x: Float = fieldRectF.left + cellSize * column
            canvas.drawLine(x, fieldRectF.top, x, fieldRectF.bottom, gridPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        for (row in 0 until ticTacToe.rows) {
            for (column in 0 until ticTacToe.columns) {
                val cell: Cell = ticTacToe.getCell(ticTacToe.Position(row, column))
                if (cell == Cell.USER_1) {
                    drawPlayer1(canvas, row, column)
                } else if (cell == Cell.USER_2) {
                    drawPlayer2(canvas, row, column)
                }
            }
        }
    }

    private fun drawPlayer1(canvas: Canvas, row: Int, column: Int) {
        val cellRect: RectF = getCellRect(row, column)
        canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.bottom, player1Paint)
        canvas.drawLine(cellRect.right, cellRect.top, cellRect.left, cellRect.bottom, player1Paint)
    }

    private fun drawPlayer2(canvas: Canvas, row: Int, column: Int) {
        val cellRect: RectF = getCellRect(row, column)
        canvas.drawCircle(cellRect.centerX(), cellRect.centerY(), cellRect.width() / 2, player2Paint)
    }

    private fun getCellRect(row: Int, column: Int): RectF {
        cellRectF.left = fieldRectF.left + column * cellSize + cellPadding
        cellRectF.top = fieldRectF.top + row * cellSize + cellPadding
        cellRectF.right = cellRectF.left + cellSize - cellPadding * 2
        cellRectF.bottom = cellRectF.top + cellSize - cellPadding * 2
        return cellRectF
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                updateClickedCell(event)
                true
            }

            MotionEvent.ACTION_UP -> {
                return performClick()
            }

            MotionEvent.ACTION_MOVE -> {
                true
            }

            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (currentColumn >= 0 && currentColumn < ticTacToe.columns && currentRow >= 0 && currentRow < ticTacToe.rows) {
            onCellClickedListener.onClick(currentRow, currentColumn, ticTacToe)
            return true
        }
        return false
    }

    private fun updateClickedCell(event: MotionEvent) {
        val row: Int = getClickedRow(event)
        val column: Int = getClickedColumn(event)
        if (row >= 0 && row < ticTacToe.rows && column >= 0 && column < ticTacToe.columns) {
            if (row != currentRow || column != currentColumn) {
                currentRow = row
                currentColumn = column
                invalidate()
            }
        }

    }

    private fun getClickedRow(event: MotionEvent): Int = ((event.y - fieldRectF.top) / cellSize).toInt()

    private fun getClickedColumn(event: MotionEvent): Int = ((event.x - fieldRectF.left) / cellSize).toInt()

    fun interface OnCellClickedListener {
        fun onClick(row: Int, column: Int, ticTacToe: TicTacToe)
    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = "#FF1100"
        const val PLAYER2_DEFAULT_COLOR = "#5A00FF"
        const val GRID_DEFAULT_COLOR = "#F0ECFF"
        const val DESIRED_CELL_SIZE = 10f
    }
}
