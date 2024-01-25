package com.herasymchuk.andrushchenko.apps.customview.fromscratch

enum class Cell {
    PLAYER_1, PLAYER_2, EMPTY
}

fun interface OnFieldChangedListener {
    fun notify(field: TicTacToeField)
}

class TicTacToeField(
    val rows: Int,
    val columns: Int
) {
    private val cells = Array(rows) { Array(columns) { Cell.EMPTY } }
    private val listeners = mutableSetOf<OnFieldChangedListener>()

    fun getCell(row: Int, column: Int): Cell {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw IllegalArgumentException("Invalid cell coordinates")
        return cells[row][column]
    }

    fun setCell(row: Int, column: Int, cell: Cell) {
        if (row < 0 || column < 0 || row >= rows || column >= columns)
            throw IllegalArgumentException("Invalid cell coordinates")
        if (cells[row][column] != cell) {
            cells[row][column] = cell
            listeners.forEach { it.notify(this) }
        }
    }

    fun subscribe(listener: OnFieldChangedListener) {
        listeners.add(listener)
    }

    fun unsubscribe(listener: OnFieldChangedListener) {
        listeners.remove(listener)
    }
}
