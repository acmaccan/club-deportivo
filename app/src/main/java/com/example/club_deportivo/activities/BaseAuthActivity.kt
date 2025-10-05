package com.example.club_deportivo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.models.User
import com.example.club_deportivo.models.UserRepository

/**
 * Una clase base abstracta para actividades que requieren un usuario autenticado.
 * Maneja la validación del ID de usuario recibido en el Intent y lo provee
 * a través de la propiedad 'user'. Si la validación falla, cierra la actividad.
 */
abstract class BaseAuthActivity : AppCompatActivity() {
    protected lateinit var user: User

    companion object {
        const val LOGGED_USER_ID_KEY = "LOGGED_USER_ID"
        const val INVALID_USER_ID = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!validateUserAndSetProperty()) {
            finish()
            return
        }
    }

    /**
     * Valida el ID de usuario del Intent, busca el usuario y lo asigna a la propiedad 'user'.
     * Muestra un Toast con un error si la validación falla.
     * @return 'true' si el usuario es válido, 'false' en caso contrario.
     */
    private fun validateUserAndSetProperty(): Boolean {
        val loggedUserId = intent.getIntExtra(LOGGED_USER_ID_KEY, INVALID_USER_ID)

        if (loggedUserId == INVALID_USER_ID) {
            Toast.makeText(this, getString(R.string.identify_user_error), Toast.LENGTH_LONG).show()
            return false
        }

        val foundUser = UserRepository.findUserById(loggedUserId)
        if (foundUser == null) {
            Toast.makeText(this, getString(R.string.not_found_user_error), Toast.LENGTH_LONG).show()
            return false
        }

        user = foundUser
        return true
    }

    /**
     * Cierra la sesión del usuario actual.
     * Limpia los SharedPreferences y redirige a LoginActivity.
     */
    protected fun logoutUser() {
        val sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(LoginActivity.USER_ID_KEY)
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finish()
    }
}
