package com.herasymchuk.andrushchenko.apps.customview.fromscratch

interface Cell {
    object Player1 : Cell
    object Player2 : Cell
    object Empty : Cell
}

typealias OnFieldChangedListener = (field: TicTacToeField) -> Unit

interface TicTacToeField {
    val rows: Int
    val columns: Int

    fun getCell(row: Int, column: Int): Cell
    fun setCell(row: Int, column: Int, cell: Cell)
    fun subscribe(listener: OnFieldChangedListener)
    fun unsubscribe(listener: OnFieldChangedListener)

    class Base(
        override val rows: Int,
        override val columns: Int,
    ) : TicTacToeField {
        private val cells = Array(rows) { Array<Cell>(columns) { Cell.Empty } }
        private val listeners = mutableSetOf<OnFieldChangedListener>()

        override fun getCell(row: Int, column: Int): Cell {
            if (row < 0 || column < 0 || row >= rows || column >= columns)
                throw IllegalArgumentException("Row or Cell is out of range")
            return cells[row][column]
        }

        override fun setCell(row: Int, column: Int, cell: Cell) {
            if (row < 0 || column < 0 || row >= rows || column >= columns)
                throw IllegalArgumentException("Row or Cell is out of range")
            if (getCell(row, column) != cell) {
                cells[row][column] = cell
                listeners.forEach { it.invoke(this) }
            }
        }

        override fun subscribe(listener: OnFieldChangedListener) {
            listeners.add(listener)
        }

        override fun unsubscribe(listener: OnFieldChangedListener) {
            listeners.remove(listener)
        }
    }

//    class Empty : TicTacToeField {
//        override val rows: Int = 0
//        override val columns: Int = 0
//        override fun getCell(row: Int, column: Int): Cell = Cell.Empty
//
//        override fun setCell(row: Int, column: Int, cell: Cell) {}
//
//        override fun subscribe(listener: OnFieldChangedListener) {}
//
//        override fun unsubscribe(listener: OnFieldChangedListener) {}
//    }
}
