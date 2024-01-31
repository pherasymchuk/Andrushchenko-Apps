package com.herasymchuk.andrushchenko.apps.customview.fromscratch.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.tictactoegame.Cell
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.ui.TicTacToeView.OnCellClickedListener
import com.herasymchuk.andrushchenko.databinding.ActivityCustomViewFromScratchBinding
import com.herasymchuk.andrushchenko.insets.applyInsets

fun interface OnSizeSelectedListener {
    fun onSizeSelected(rows: Int, columns: Int)
}

class CustomViewFromScratchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomViewFromScratchBinding
    private var currentPlayer: Cell = Cell.USER_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCustomViewFromScratchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)

        binding.ticTacToe.onCellClickedListener = OnCellClickedListener { row, column, ticTacToe ->
            val cell: Cell = ticTacToe.getCell(ticTacToe.Position(row, column))
            if (cell == Cell.EMPTY) {
                ticTacToe.setCell(ticTacToe.Position(row, column), currentPlayer)
                changePlayer()
            }
            if (ticTacToe.isFull()) {
                Toast.makeText(this, "The game is over", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectFieldSize(onSizeSelectedListener: OnSizeSelectedListener) {
        TODO()
    }

    private fun changePlayer() {
        currentPlayer = when (currentPlayer) {
            Cell.EMPTY -> throw IllegalStateException("Player should not be EMPTY")
            Cell.USER_1 -> Cell.USER_2
            else -> Cell.USER_1
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
