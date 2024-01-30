package com.herasymchuk.andrushchenko.apps.customview.fromscratch

enum class Cell {
    PLAYER_1, PLAYER_2, EMPTY
}

fun interface OnFieldChangedListener {
    fun notify(field: TicTacToeField)
}

class TicTacToeField(
    val rows: Int, val columns: Int
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

    fun checkWinCondition(requiredCellsInRow: Int): Cell {
        // Check each row for a win condition
        for (rowIndex in 0 until rows) {
            for (columnIndex in 0 until columns - requiredCellsInRow + 1) {
                val currentCell = cells[rowIndex][columnIndex]
                if (currentCell != Cell.EMPTY && (0 until requiredCellsInRow)
                        .all { cells[rowIndex][columnIndex + it] == currentCell }
                ) {
                    return currentCell
                }
            }
        }

        // Check each column for a win condition
        for (rowIndex in 0 until rows - requiredCellsInRow + 1) {
            for (columnIndex in 0 until columns) {
                val currentCell = cells[rowIndex][columnIndex]
                if (currentCell != Cell.EMPTY && (0 until requiredCellsInRow)
                        .all { cells[rowIndex + it][columnIndex] == currentCell }
                ) {
                    return currentCell
                }
            }
        }

        // Check diagonals from top-left to bottom-right for a win condition
        for (rowIndex in 0 until rows - requiredCellsInRow + 1) {
            for (columnIndex in 0 until columns - requiredCellsInRow + 1) {
                val currentCell = cells[rowIndex][columnIndex]
                if (currentCell != Cell.EMPTY && (0 until requiredCellsInRow)
                        .all { cells[rowIndex + it][columnIndex + it] == currentCell }
                ) {
                    return currentCell
                }
            }
        }

        // Check diagonals from top-right to bottom-left for a win condition
        for (rowIndex in 0 until rows - requiredCellsInRow + 1) {
            for (columnIndex in requiredCellsInRow - 1 until columns) {
                val currentCell = cells[rowIndex][columnIndex]
                if (currentCell != Cell.EMPTY && (0 until requiredCellsInRow)
                        .all { cells[rowIndex + it][columnIndex - it] == currentCell }
                ) {
                    return currentCell
                }
            }
        }

        // If no win condition is met, return Cell.EMPTY
        return Cell.EMPTY
    }

}
