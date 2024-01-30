package com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame

fun interface FieldObserver {
    fun onFieldChanged(field: List<List<Cell>>)
}

interface ObservableField {
    fun subscribe(observer: FieldObserver)
    fun unsubscribe(observer: FieldObserver)
}

abstract class TicTacToe(protected val rows: Int, protected val columns: Int) {
    abstract fun getCell(position: TicTacToe.Position): Cell

    abstract fun setCell(position: TicTacToe.Position, cell: Cell): Boolean
    abstract val field: List<List<Cell>>

    class Base(row: Int, columns: Int) : TicTacToe(row, columns), ObservableField {
        override val field: MutableList<MutableList<Cell>> = MutableList(rows) { MutableList(columns) { Cell.EMPTY } }
        private val observers: MutableSet<FieldObserver> = mutableSetOf()

        override fun getCell(position: TicTacToe.Position): Cell {
            return field[position.row][position.column]
        }

        override fun setCell(position: TicTacToe.Position, cell: Cell): Boolean {
            field[position.row][position.column] = cell
            observers.forEach { it.onFieldChanged(field) }
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
