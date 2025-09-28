package com.example.club_deportivo.models

import android.text.InputType
import com.google.android.material.textfield.TextInputLayout

data class InputConfig (
    val customValidator: ((String) -> Boolean)? = null,
    val endIconMode: Int = TextInputLayout.END_ICON_NONE,
    val hintStringId: Int,
    val inputType: Int = InputType.TYPE_CLASS_TEXT,
    val isRequired: Boolean = true,
    val layoutId: Int,
    val minLength: Int = 0
)