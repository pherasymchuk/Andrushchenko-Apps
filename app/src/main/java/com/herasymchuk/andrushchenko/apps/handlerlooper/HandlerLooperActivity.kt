package com.herasymchuk.andrushchenko.apps.handlerlooper

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.databinding.ActivityHandlerLooperBinding
import com.herasymchuk.andrushchenko.insets.applyInsets
import kotlin.random.Random

class HandlerLooperActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHandlerLooperBinding
    private val handler = Handler(Looper.getMainLooper())
//    private val token = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHandlerLooperBinding.inflate(layoutInflater)
        binding.root.applyInsets(top = true, left = true, right = true, bottom = true)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.root.forEach {
            if (it is Button) it.setOnClickListener(universalButtonListener)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }

    private fun toggleTestButtonState() {
        binding.testButton.isEnabled = !binding.testButton.isEnabled
    }

    private fun nextRandomColor() {
        val randomColor = -Random.nextInt(255 * 255 * 255)
        binding.colorView.setBackgroundColor(randomColor)
    }

    private fun showToast() {
        Toast.makeText(this, R.string.hello, Toast.LENGTH_SHORT).show()
    }

    private val nextRandomColorRunnable = Runnable {
        nextRandomColor()
    }

    private val showToastRunnable = Runnable {
        showToast()
    }

    private val universalButtonListener = View.OnClickListener {
        Thread {
            when (it.id) {

                R.id.enableDisableButton ->
                    handler.post { toggleTestButtonState() }

                R.id.randomColorButton ->
                    handler.post { nextRandomColor() }

                R.id.enableDisableDelayedButton ->
                    handler.postDelayed({ toggleTestButtonState() }, DELAY)

                R.id.randomColorDelayedButton ->
                    handler.postDelayed({ nextRandomColor() }, DELAY)

                R.id.randomColorTokenDelayedButton ->
                    handler.postDelayed(nextRandomColorRunnable, DELAY)

                R.id.showToastButton ->
                    handler.postDelayed(showToastRunnable, DELAY)

                R.id.cancelButton -> {
                    handler.removeCallbacks(nextRandomColorRunnable)
                    handler.removeCallbacks(showToastRunnable)
                }

            }
        }.start()

    }

    companion object {
        @JvmStatic
        private val DELAY = 2000L // milliseconds
    }

}
