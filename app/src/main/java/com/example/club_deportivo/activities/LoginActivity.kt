package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.models.UserRepository
import com.example.club_deportivo.models.UserRole
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById<TextInputLayout>(R.id.emailInput).editText as TextInputEditText
        passwordInput = findViewById<TextInputLayout>(R.id.passwordInput).editText as TextInputEditText
        loginButton = findViewById(R.id.loginButton)
        registerText = findViewById(R.id.registerText)

        setupInputs()

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
        emailInputLayout.hint = getString(R.string.email)
        emailInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInput)
        passwordInputLayout.hint = getString(R.string.password)
        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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

        val user = UserRepository.findUserByCredentials(email, password)

        if (user != null) {
            when (user.userType) {
                UserRole.ADMIN -> {
                    navigateToActivity(AdminActivity::class.java, user.id)
                }
                UserRole.CLIENT -> {
                    navigateToActivity(HomeActivity::class.java, user.id)
                }
            }
        } else {
            findViewById<TextInputLayout>(R.id.passwordInput).error = "Email o contraseña incorrectos"
        }
    }

    private fun navigateToActivity(destination: Class<*>, userId: Int) {
        val intent = Intent(this, destination)

        intent.putExtra("LOGGED_USER_ID", userId)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}