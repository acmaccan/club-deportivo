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
    companion object {
        const val PREFS_NAME = "ClubDeportivoPrefs"
        const val USER_ID_KEY = "LOGGED_USER_ID"
    }

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: MaterialButton
    private lateinit var registerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews()
        setupInputs()
        setupListeners()
    }

    /**
     * Inicializa las propiedades de las vistas encontrándolas por su ID.
     */
    private fun initializeViews() {
        emailInputLayout = findViewById(R.id.emailInput)
        passwordInputLayout = findViewById(R.id.passwordInput)
        emailInput = emailInputLayout.editText as TextInputEditText
        passwordInput = passwordInputLayout.editText as TextInputEditText
        loginButton = findViewById(R.id.loginButton)
        registerText = findViewById(R.id.registerText)
    }

    private fun setupInputs() {
        emailInputLayout.hint = getString(R.string.email)
        emailInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        passwordInputLayout.hint = getString(R.string.password)
        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }

    /**
     * Configura los listeners para los elementos interactivos de la pantalla.
     */
    private fun setupListeners() {
        loginButton.setOnClickListener {
            handleLogin()
        }

        registerText.setOnClickListener {
            navigateToActivity(RegisterActivity::class.java)
        }
    }

    /**
     * Valida las credenciales ingresadas por el usuario, muestra errores si es necesario
     * y navega a la pantalla correspondiente si el inicio de sesión es exitoso.
     */
    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        emailInputLayout.error = null
        passwordInputLayout.error = null

        if (email.isEmpty()) {
            emailInputLayout.error = getString(R.string.enter_your_email)
            return
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = getString(R.string.enter_your_password)
            return
        }

        val user = UserRepository.findUserByCredentials(email, password)

        if (user != null) {
            val destination = when (user.userType) {
                UserRole.ADMIN -> AdminActivity::class.java
                UserRole.CLIENT -> HomeActivity::class.java
            }
            navigateToActivity(destination, user.id)
        } else {
            passwordInputLayout.error = getString(R.string.wrong_credentials)
        }
    }

    /**
     * Navega a una actividad de destino autenticada, pasando el ID de usuario.
     * Cierra la actividad actual para que el usuario no pueda volver atrás.
     * @param destination La clase de la actividad a la que se navegará.
     * @param userId El ID del usuario que ha iniciado sesión.
     */
    private fun navigateToActivity(destination: Class<*>, userId: Int) {
        val intent = Intent(this, destination)

        intent.putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, userId)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Sobrecarga de la función para navegar a actividades que no requieren autenticación,
     * como la pantalla de registro.
     * @param destination La clase de la actividad a la que se navegará.
     */
    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }
}