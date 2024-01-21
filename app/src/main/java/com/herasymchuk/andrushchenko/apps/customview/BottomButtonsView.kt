package com.herasymchuk.andrushchenko.apps.customview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.databinding.PartButtonsBinding

fun interface BottomButtonsOnClickListener {
    fun onClick()
}

class BottomButtonsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bottomButtonsStyle,
    defStyleRes: Int = R.style.DefaultMyButtonsStyle,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding: PartButtonsBinding

    var isProgressMode: Boolean = false
        set(value) {
            field = value
            with(binding) {
                if (value) {
                    btnPositive.visibility = INVISIBLE
                    btnNegative.visibility = INVISIBLE
                    progress.visibility = VISIBLE
                } else {
                    btnPositive.visibility = VISIBLE
                    btnNegative.visibility = VISIBLE
                    progress.visibility = GONE
                }
            }
        }

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.part_buttons, this, true)
        binding = PartButtonsBinding.bind(this)
        initializeAttrs(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.BottomButtonsView, defStyleAttr, defStyleRes
        )

        with(binding) {
            val positiveButtonText: String? =
                typedArray.getString(R.styleable.BottomButtonsView_bottomPositiveButtonText)
            setPositiveButtonText(positiveButtonText ?: context.getString(R.string.ok))

            val negativeButtonText: String? =
                typedArray.getString(R.styleable.BottomButtonsView_bottomNegativeButtonBackground)
            setNegativeButtonText(negativeButtonText ?: context.getString(R.string.cancel))
            btnNegative.text = negativeButtonText ?: context.getString(R.string.cancel)

            val positiveButtonColor: Int = typedArray.getColor(
                R.styleable.BottomButtonsView_bottomPositiveButtonBackground, Color.BLACK
            )
            btnPositive.backgroundTintList = ColorStateList.valueOf(positiveButtonColor)

            val negativeButtonColor: Int = typedArray.getColor(
                R.styleable.BottomButtonsView_bottomNegativeButtonBackground, Color.WHITE
            )
            btnNegative.backgroundTintList = ColorStateList.valueOf(negativeButtonColor)

            isProgressMode =
                typedArray.getBoolean(R.styleable.BottomButtonsView_bottomProgressMode, false)

        }
        typedArray.recycle()
    }

    fun setPositiveButtonListener(listener: BottomButtonsOnClickListener) {
        binding.btnPositive.setOnClickListener {
            listener.onClick()
        }
    }

    fun setNegativeButtonListener(listener: BottomButtonsOnClickListener) {
        binding.btnNegative.setOnClickListener {
            listener.onClick()
        }
    }

    fun setPositiveButtonText(text: String) {
        binding.btnPositive.text = text
    }

    fun setNegativeButtonText(text: String) {
        binding.btnNegative.text = text
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState() ?: BaseSavedState.EMPTY_STATE
        val savedState = SavedState(superState)
        savedState.positiveButtonText = binding.btnPositive.text.toString()
        savedState.negativeButtonText = binding.btnNegative.text.toString()
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState: SavedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        binding.btnPositive.text = savedState.positiveButtonText
        binding.btnNegative.text = savedState.negativeButtonText
    }

    class SavedState : BaseSavedState {
        var positiveButtonText = ""
        var negativeButtonText = ""

        constructor(superState: Parcelable) : super(superState)
        constructor(source: Parcel) : super(source) {
            positiveButtonText = source.readString() ?: positiveButtonText
            negativeButtonText = source.readString() ?: negativeButtonText
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(positiveButtonText)
            out.writeString(negativeButtonText)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel): SavedState = SavedState(source)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

        }
    }
}
