package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerButton = findViewById<MaterialButton>(R.id.registerButton)
        val loginText = findViewById<TextView>(R.id.loginText)

        registerButton.setOnClickListener {
            // val intent = Intent(this, RegisterActivity::class.java)
            // TODO: change
            val intent = Intent(this, UploadMedicalDocumentActivity::class.java)
            startActivity(intent)
        }

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}