package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.models.UserRole
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

    private val memberEmail = "member@sportclub.com"
    private val memberPassword = "member123456"

    private val noMemberEmail = "no-member@sportclub.com"
    private val noMemberPassword = "noMember123456"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupInputs()

        emailInput = findViewById<TextInputLayout>(R.id.emailInput).editText as TextInputEditText
        passwordInput = findViewById<TextInputLayout>(R.id.passwordInput).editText as TextInputEditText
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

        when (isValidLogin(email, password)) {
            UserRole.ADMIN -> {
                navigateToActivity(AdminActivity::class.java)
            }
            UserRole.MEMBER -> {
                Toast.makeText(this, "¡Bienvenido, socio!", Toast.LENGTH_SHORT).show()
                navigateToActivity(HomeActivity::class.java)
            }
            UserRole.NO_MEMBER -> {
                Toast.makeText(this, "¡Bienvenido, no socio!", Toast.LENGTH_SHORT).show()
                navigateToActivity(HomeActivity::class.java)
            }
            UserRole.INVALID -> {
                findViewById<TextInputLayout>(R.id.passwordInput).error = "Email o contraseña incorrectos"
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidLogin(email: String, password: String): UserRole {
        return when {
            email == adminEmail && password == adminPassword -> UserRole.ADMIN
            email == memberEmail && password == memberPassword -> UserRole.MEMBER
            email == noMemberEmail && password == noMemberPassword -> UserRole.NO_MEMBER
            else -> UserRole.INVALID
        }
    }

    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}