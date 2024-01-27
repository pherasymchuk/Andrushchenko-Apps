package com.herasymchuk.andrushchenko.apps.customview.fromscratch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
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

    private var fieldRect = RectF()
    private var cellSize: Float = 0f
    private var cellPadding: Float = 0f
    private val cellRect = RectF()

    private var ticTacToeField: TicTacToeField? = null
        set(value) {
            field?.unsubscribe(onFieldChangedListener)
            field = value
            field?.subscribe(onFieldChangedListener)
            updateViewSizes()
            requestLayout()
            invalidate()
        }

    private var player1Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = player1Color
        style = Paint.Style.STROKE
        strokeWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
    }
    private var player2Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = player2Color
        style = Paint.Style.STROKE
        strokeWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics)
    }
    private var gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = gridColor
        style = Paint.Style.STROKE
        strokeWidth =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, resources.displayMetrics)
    }

    private val onFieldChangedListener = OnFieldChangedListener {

    }

    init {
        if (attrs != null) {
            initAttrs(attrs, defStyleAttr, defStyleRes)
        }
        if (isInEditMode) {
            ticTacToeField = TicTacToeField(8, 6)
            ticTacToeField?.setCell(4, 2, Cell.PLAYER_1)
            ticTacToeField?.setCell(5, 2, Cell.PLAYER_2)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ticTacToeField?.subscribe(onFieldChangedListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ticTacToeField?.unsubscribe(onFieldChangedListener)
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.TicTacToeView, defStyleAttr, defStyleRes
        )
        player1Color = typedArray.getColor(R.styleable.TicTacToeView_player1Color, player1Color)
        player2Color = typedArray.getColor(R.styleable.TicTacToeView_player2Color, player2Color)
        gridColor = typedArray.getColor(R.styleable.TicTacToeView_gridColor, gridColor)
        typedArray.recycle()
    }

    /**
     * Все ещё можно договорится с компоновщиком о размере
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val desiredCellSizeInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, DESIRED_CELL_SIZE, resources.displayMetrics
        ).toInt()
        val rows = ticTacToeField?.rows ?: 0
        val columns = ticTacToeField?.columns ?: 0

        val desiredWidthInPixels =
            maxOf(minWidth, columns * desiredCellSizeInPixels + paddingLeft + paddingRight)
        val desiredHeightInPixels =
            maxOf(minHeight, rows * desiredCellSizeInPixels + paddingTop + paddingBottom)

        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthSpecMode) {
            MeasureSpec.EXACTLY -> widthSpecSize
            MeasureSpec.AT_MOST -> minOf(desiredWidthInPixels, widthSpecSize)
            else -> desiredWidthInPixels
        }
        val height = when (heightSpecMode) {
            MeasureSpec.EXACTLY -> heightSpecSize
            MeasureSpec.AT_MOST -> minOf(desiredHeightInPixels, heightSpecSize)
            else -> desiredHeightInPixels
        }
        setMeasuredDimension(width, height)
    }

    /**
     * Вызивается после метода onMeasure,
     * когда компоновщик уже назначил определенный размер компоненту
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViewSizes()
    }

    private fun updateViewSizes() {
        if (ticTacToeField == null) return

        val safeWidth = width - paddingLeft - paddingRight
        val safeHeight = height - paddingTop - paddingBottom

        ticTacToeField?.let {
            if (it.rows < 3 || it.columns < 3) {
                throw IllegalStateException("Rows and columns should not be less than 3")
            }
        }
        cellSize = minOf(
            safeWidth / ticTacToeField!!.columns.toFloat(),
            safeHeight / ticTacToeField!!.rows.toFloat()
        )
        cellPadding = cellSize * 0.2f

        val fieldWidth = cellSize * ticTacToeField!!.columns
        val fieldHeight = cellSize * ticTacToeField!!.rows

        fieldRect.left = paddingLeft + (safeWidth - fieldWidth) / 2
        fieldRect.top = paddingTop + (safeHeight - fieldHeight) / 2
        fieldRect.right = fieldRect.left + fieldWidth
        fieldRect.bottom = fieldRect.top + fieldHeight
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (ticTacToeField == null) return
        if (cellSize == 0f) return
        if (fieldRect.width() <= 0) return
        if (fieldRect.height() <= 0) return
        drawGrid(canvas)
        drawCells(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val field: TicTacToeField = ticTacToeField ?: return
        val startX = fieldRect.left
        val endX = fieldRect.right
        for (row in 0..field.rows) {
            val y = fieldRect.top + cellSize * row
            canvas.drawLine(startX, y, endX, y, gridPaint)
        }

        val startY = fieldRect.top
        val endY = fieldRect.bottom
        for (column in 0..field.columns) {
            val x = fieldRect.left + cellSize * column
            canvas.drawLine(x, startY, x, endY, gridPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        val field = ticTacToeField ?: return
        for (row in 0 until field.rows) {
            for (column in 0 until field.columns) {
                val cell = field.getCell(row, column)
                if (cell == Cell.PLAYER_1) {
                    drawPlayer1(canvas, row, column)
                } else if (cell == Cell.PLAYER_2) {
                    drawPlayer2(canvas, row, column)
                }
            }
        }
    }

    private fun drawPlayer1(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawLine(cellRect.left, cellRect.top, cellRect.right, cellRect.bottom, player1Paint)
        canvas.drawLine(cellRect.right, cellRect.top, cellRect.left, cellRect.bottom, player1Paint)
    }

    private fun drawPlayer2(canvas: Canvas, row: Int, column: Int) {
        val cellRect = getCellRect(row, column)
        canvas.drawCircle(
            cellRect.centerX(),
            cellRect.centerY(),
            cellRect.width() / 2,
            player2Paint
        )
    }

    private fun getCellRect(row: Int, column: Int): RectF {
        cellRect.left = fieldRect.left + column * cellSize + cellPadding
        cellRect.top = fieldRect.top + row * cellSize + cellPadding
        cellRect.right = cellRect.left + cellSize - cellPadding * 2
        cellRect.bottom = cellRect.top + cellSize - cellPadding * 2
        return cellRect
    }

    companion object {
        const val PLAYER1_DEFAULT_COLOR = "#008800"
        const val PLAYER2_DEFAULT_COLOR = "#880000"
        const val GRID_DEFAULT_COLOR = "#dfdfdf"

        const val DESIRED_CELL_SIZE = 50f
    }
}
