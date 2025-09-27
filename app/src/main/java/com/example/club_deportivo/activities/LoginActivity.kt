package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: TextView

    private val adminEmail = "admin@sportclub.com"
    private val adminPassword = "admin123456"

    private val clientEmail = "client@sportclub.com"
    private val clientPassword = "client123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupInputs()

        loginButton = findViewById(R.id.loginButton)
        registerText = findViewById(R.id.registerText)

        loginButton.setOnClickListener {
            handleLogin()
        }

        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupInputs() {
        val emailInputLayout = findViewById<TextInputLayout>(R.id.emailInput)
        val emailEditText = emailInputLayout.editText as? TextInputEditText

        emailInputLayout.hint = getString(R.string.email)
        emailEditText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInput)
        val passwordEditText = passwordInputLayout.editText as? TextInputEditText

        passwordInputLayout.hint = getString(R.string.password)
        passwordEditText?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        passwordInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty()) {
            findViewById<TextInputLayout>(R.id.emailInput).error = "Ingresa tu email"
            return
        }

        if (password.isEmpty()) {
            findViewById<TextInputLayout>(R.id.passwordInput).error = "Ingresa una contraseña válida"
            return
        }

        findViewById<TextInputLayout>(R.id.emailInput).error = null
        findViewById<TextInputLayout>(R.id.passwordInput).error = null

        if (isValidLogin(email, password)) {
            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
            navigateToHomeScreen()
        } else {
            findViewById<TextInputLayout>(R.id.passwordInput).error = "Email o contraseña incorrectos"
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidLogin(email: String, password: String): Boolean {
        return when {
            email == adminEmail && password == adminPassword -> true
            email == clientEmail && password == clientPassword -> true
            else -> false
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}