package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

data class InputConfig (
    val layoutId: Int,
    val hintStringId: Int,
    val inputType: Int = InputType.TYPE_CLASS_TEXT,
    val endIconMode: Int = TextInputLayout.END_ICON_NONE
)

class RegisterActivity : AppCompatActivity() {
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var documentInput: TextInputEditText
    private lateinit var phoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    private lateinit var registerButton: MaterialButton
    private lateinit var registerCancelButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupInputs()

        registerButton = findViewById<MaterialButton>(R.id.registerButton)
        registerCancelButton = findViewById<MaterialButton>(R.id.registerCancelButton)

        registerButton.setOnClickListener {
           handleRegister()
        }

        registerCancelButton.setOnClickListener {
            finish()
        }
    }

    private fun setupInputs() {
        val inputConfigs = listOf<InputConfig>(
            InputConfig(
                layoutId = R.id.fullNameInput,
                hintStringId = R.string.register_full_name_input
            ),
            InputConfig(
                layoutId = R.id.documentInput,
                hintStringId = R.string.register_document_input
            ),
            InputConfig(
                layoutId = R.id.phoneInput,
                hintStringId = R.string.register_phone_input,
                inputType = InputType.TYPE_CLASS_PHONE
            ),
            InputConfig(
                layoutId = R.id.passwordInput,
                hintStringId = R.string.register_password_input,
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            )
        )

        inputConfigs.forEach { config ->
            setupTextInput(config)
        }
    }

    private fun setupTextInput(config: InputConfig) {
        val inputLayout = findViewById<TextInputLayout>(config.layoutId)
        val editText = inputLayout.editText as? TextInputEditText

        inputLayout.hint = getString(config.hintStringId)
        editText?.inputType = config.inputType

        if (config.endIconMode != TextInputLayout.END_ICON_NONE) {
            inputLayout.endIconMode = config.endIconMode
        }
    }

    private fun handleRegister() {
        println("Registro exitoso")
    }
}