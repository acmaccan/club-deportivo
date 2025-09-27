package com.example.club_deportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import android.widget.TextView
import android.widget.Toast

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
            Toast.makeText(this, "Funcionalidad de registro próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupInputs() {
        val emailLayout = findViewById<TextInputLayout>(R.id.emailInput)
        emailInput = emailLayout.findViewById(R.id.editText)
        emailLayout.hint = "Email"
        emailInput.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInput)
        passwordInput = passwordLayout.findViewById(R.id.editText)
        passwordLayout.hint = "Contraseña"
        passwordLayout.endIconMode = com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE
        passwordInput.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
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