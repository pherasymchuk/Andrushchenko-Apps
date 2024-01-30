package com.herasymchuk.andrushchenko.apps.customview.fromscratch

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.herasymchuk.andrushchenko.databinding.ActivityCustomViewFromScratchBinding

class CustomViewFromScratchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomViewFromScratchBinding
    private var currentPlayer: Cell = Cell.PLAYER_1
    private var requiredCellsInRow: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomViewFromScratchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ticTacToeField.ticTacToeField = TicTacToeField(5, 5)

        binding.ticTacToeField.actionListener = OnCellActionListener { row, column, field ->
            val cell = field.getCell(row, column)
            if (cell == Cell.EMPTY) {
                field.setCell(row, column, currentPlayer)
                val winner = field.checkWinCondition(requiredCellsInRow)
                if (winner != Cell.EMPTY) {
                    //Todo Handle win condition here
                    //For example, you could display a dialog to the user
                    MaterialAlertDialogBuilder(this).apply {
                        setTitle("Game Over")
                        setMessage("Player ${if (winner == Cell.PLAYER_1) "1" else "2"} wins!")
                        setPositiveButton("Ok") { _: DialogInterface, i: Int ->
                        }
                        show()
                    }
                } else {
                    changePlayer()
                }
            }
        }
    }

    private fun changePlayer() {
        currentPlayer = when (currentPlayer) {
            Cell.EMPTY -> throw IllegalArgumentException("Player cannot be empty")
            Cell.PLAYER_1 -> Cell.PLAYER_2
            Cell.PLAYER_2 -> Cell.PLAYER_1
        }
    }
}
