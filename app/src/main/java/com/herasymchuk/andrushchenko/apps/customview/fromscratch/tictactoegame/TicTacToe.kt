package com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame

interface Board {
    fun getCell(position: Position): Cell
    fun setCell(position: Position, cell: Cell): Boolean
}

data class Position(
    val row: Int,
    val column: Int,
    private val maxRow: Int,
    private val maxColumn: Int
) {
    init {
        if (row < 0 || row >= maxRow || column < 0 || column >= maxColumn) {
            throw IllegalStateException("Row or column is out of bounds")
        }
    }
}

class TicTacToe(private val rows: Int, private val columns: Int) : Board {

    private val field = Array(rows) { Array(columns) { Cell.EMPTY } }
    override fun getCell(position: Position): Cell {
        return field[position.row][position.column]
    }

    override fun setCell(position: Position, cell: Cell): Boolean {
        field[position.row][position.column] = cell
        return true
    }
}
