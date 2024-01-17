package com.herasymchuk.andrushchenko.apps.customview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.databinding.PartButtonsBinding

//class BottomButtonsView(
//    context: Context,
//    attrs: AttributeSet?,
//    defStyleAttr: Int,
//    defStyleRes: Int,
//) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
//        context,
//        attrs,
//        defStyleAttr,
//        0
//    )
//
//    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
//    constructor(context: Context) : this(context, null)


class BottomButtonsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: PartButtonsBinding

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)
        initializeAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BottomButtonsView,
            defStyleAttr,
            defStyleRes
        )

        with(binding) {
            val positiveButtonText: String? =
                typedArray.getString(R.styleable.BottomButtonsView_bottomPositiveButtonText)
            btnPositive.text = positiveButtonText ?: context.getString(R.string.ok)

            val negativeButtonText: String? =
                typedArray.getString(R.styleable.BottomButtonsView_bottomNegativeButtonBackground)
            btnNegative.text = negativeButtonText ?: context.getString(R.string.cancel)

            val positiveButtonColor: Int =
                typedArray.getColor(
                    R.styleable.BottomButtonsView_bottomPositiveButtonBackground,
                    Color.BLACK
                )
            btnPositive.backgroundTintList = ColorStateList.valueOf(positiveButtonColor)

            val negativeButtonColor: Int =
                typedArray.getColor(
                    R.styleable.BottomButtonsView_bottomNegativeButtonBackground,
                    Color.WHITE
                )
            btnNegative.backgroundTintList = ColorStateList.valueOf(negativeButtonColor)

            val isProgressMode =
                typedArray.getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)
            if (isProgressMode) {
                btnPositive.visibility = INVISIBLE
                btnNegative.visibility = INVISIBLE
                progress.visibility = VISIBLE
            } else {
                btnPositive.visibility = VISIBLE
                btnNegative.visibility = VISIBLE
                progress.visibility = GONE
            }
        }

        typedArray.recycle()
    }
}
