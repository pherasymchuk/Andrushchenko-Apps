package com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame

fun interface FieldObserver {
    fun onFieldChanged(field: List<List<Cell>>)
}

interface Board {
    fun getCell(position: TicTacToe.Position): Cell

    fun setCell(position: TicTacToe.Position, cell: Cell): Boolean
    val field: List<List<Cell>>
}

class TicTacToe(private val rows: Int, private val columns: Int) : Board {
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

    fun subscribe(fieldObserver: FieldObserver) {
        observers.add(fieldObserver)
    }

    fun unsubscribe(fieldObserver: FieldObserver) {
        observers.remove(fieldObserver)
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
