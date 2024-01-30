package com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame

fun interface FieldObserver {
    fun onFieldChanged(ticTacToe: TicTacToe)
}

interface ObservableField {
    fun subscribe(observer: FieldObserver)
    fun unsubscribe(observer: FieldObserver)
}

abstract class TicTacToe(val rows: Int, val columns: Int) : ObservableField {

    abstract fun getCell(position: TicTacToe.Position): Cell

    abstract fun setCell(position: TicTacToe.Position, cell: Cell): Boolean
    abstract val field: List<List<Cell>>

    class Base(row: Int, columns: Int) : TicTacToe(row, columns) {
        override val field: MutableList<MutableList<Cell>> = MutableList(rows) { MutableList(columns) { Cell.EMPTY } }
        private val observers: MutableSet<FieldObserver> = mutableSetOf()

        init {
            if (row < 3 || columns < 3) throw IllegalArgumentException("Game field is too small, should not be less than 3")
            if (row > 20 || columns > 20) throw IllegalArgumentException("Game field is too big, should be more than 20")
        }

        override fun getCell(position: TicTacToe.Position): Cell {
            return field[position.row][position.column]
        }

        override fun setCell(position: TicTacToe.Position, cell: Cell): Boolean {
            field[position.row][position.column] = cell
            observers.forEach { it.onFieldChanged(this) }
            return true
        }

        override fun subscribe(observer: FieldObserver) {
            observers.add(observer)
        }

        override fun unsubscribe(observer: FieldObserver) {
            observers.remove(observer)
        }
    }


    inner class Position(
        val row: Int,
        val column: Int
    ) {
        init {
            if (row < 0 || row >= rows || column < 0 || column >= columns) {
                throw IllegalStateException("Row or column is out of bounds")
            }
        }
    }
}
