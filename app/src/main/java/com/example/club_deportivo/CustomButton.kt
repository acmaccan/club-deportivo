package com.example.club_deportivo

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    enum class ButtonColor { PRIMARY, DANGER, WHITE }
    enum class ButtonVariant { FILLED, TEXT }
    enum class ButtonState { ENABLED, DISABLED }

    private var buttonColor: ButtonColor = ButtonColor.PRIMARY
    private var buttonVariant: ButtonVariant = ButtonVariant.FILLED
    private var buttonState: ButtonState = ButtonState.ENABLED

    init {
        attrs?.let { setupAttributes(it) }
        applyButtonStyle()
    }

    private fun setupAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton)
        
        try {
            text = typedArray.getString(R.styleable.CustomButton_buttonText) ?: ""
            
            buttonColor = ButtonColor.entries[
                typedArray.getInt(R.styleable.CustomButton_buttonColor, 0)
            ]
            
            buttonVariant = ButtonVariant.entries[
                typedArray.getInt(R.styleable.CustomButton_buttonVariant, 0)
            ]
            
            buttonState = ButtonState.entries[
                typedArray.getInt(R.styleable.CustomButton_buttonState, 0)
            ]
            
            val cornerRadius = typedArray.getDimension(R.styleable.CustomButton_buttonCornerRadius, 12f)
            setCornerRadius(cornerRadius)
            
        } finally {
            typedArray.recycle()
        }
    }

    private fun applyButtonStyle() {
        when (buttonVariant) {
            ButtonVariant.FILLED -> applyFilledStyle()
            ButtonVariant.TEXT -> applyTextStyle()
        }
        
        updateButtonState()
    }

    private fun applyFilledStyle() {
        backgroundTintList = null
        when (buttonColor) {
            ButtonColor.PRIMARY -> {
                background = ContextCompat.getDrawable(context, R.drawable.custom_button_primary)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            ButtonColor.DANGER -> {
                background = ContextCompat.getDrawable(context, R.drawable.custom_button_danger)
                setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            ButtonColor.WHITE -> {
                background = ContextCompat.getDrawable(context, R.drawable.custom_button_white)
                setTextColor(ContextCompat.getColor(context, R.color.secondary_main))
            }
        }
        applyCommonStyle()
    }

    private fun applyTextStyle() {
        backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        when (buttonColor) {
            ButtonColor.PRIMARY -> setTextColor(ContextCompat.getColor(context, R.color.primary_main))
            ButtonColor.DANGER -> setTextColor(ContextCompat.getColor(context, R.color.error_main))
            ButtonColor.WHITE -> setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        applyCommonStyle()
    }

    private fun applyCommonStyle() {
        strokeWidth = 0
        textSize = 16f
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }

    private fun updateButtonState() {
        isEnabled = buttonState == ButtonState.ENABLED
        alpha = if (buttonState == ButtonState.ENABLED) 1.0f else 0.6f
        
        if (buttonState == ButtonState.DISABLED) {
            setTextColor(ContextCompat.getColor(context, R.color.neutral_main))
        }
    }

    private fun setCornerRadius(radius: Float) {
        cornerRadius = radius.toInt()
    }
}