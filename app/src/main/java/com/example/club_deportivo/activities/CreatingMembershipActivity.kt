package com.example.club_deportivo.activities

import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R

class CreatingMembershipActivity : AppCompatActivity() {

    companion object {
        private const val LOADING_DURATION_MS = 4000L
    }

    private lateinit var loadingIv: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_membership)

        initializeViews()
        startLoadingProcess()
    }

    /**
     * Inicializa las propiedades de las vistas encontrándolas por su ID.
     */
    private fun initializeViews() {
        loadingIv = findViewById(R.id.loadingAnimation)
    }

    /**
     * Inicia el proceso de carga simulado y navega al home al completar.
     */
    private fun startLoadingProcess() {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToHome()
        }, LOADING_DURATION_MS)
    }

    /**
     * Arranca la animación del vector cuando la Activity está visible.
     */
    override fun onStart() {
        super.onStart()
        (loadingIv.drawable as? Animatable)?.start()
    }

    /**
     * Detiene la animación del vector cuando la Activity deja de estar visible.
     */
    override fun onStop() {
        (loadingIv.drawable as? Animatable)?.stop()
        super.onStop()
    }

    /**
     * Navega a la pantalla principal.
     * Cierra la actividad actual para que el usuario no pueda volver atrás.
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
