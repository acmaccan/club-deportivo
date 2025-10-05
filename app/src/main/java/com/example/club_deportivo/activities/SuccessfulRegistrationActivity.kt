package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.ui.CustomButton

class SuccessfulRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_registration)
        
        val continueButton = findViewById<CustomButton>(R.id.continueButton)
        continueButton.setOnClickListener {
            val intent = Intent(this, UploadMedicalDocumentActivity::class.java)

            val userId = getIntent().getIntExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, BaseAuthActivity.INVALID_USER_ID)
            if (userId != BaseAuthActivity.INVALID_USER_ID) {
                intent.putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, userId)
            }
            startActivity(intent)
            finish()
        }
    }
}