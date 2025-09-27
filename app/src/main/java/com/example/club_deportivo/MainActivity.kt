package com.example.club_deportivo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerButton = findViewById<MaterialButton>(R.id.registerButton)
        val loginText = findViewById<TextView>(R.id.loginText)

        registerButton.setOnClickListener {
            // TODO: Navegar a pantalla de registro
            println("Botón Registrarse clickeado")
        }

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}