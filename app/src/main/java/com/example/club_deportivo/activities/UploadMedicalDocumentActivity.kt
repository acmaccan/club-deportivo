package com.example.club_deportivo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.ui.CustomButton
import com.google.android.material.card.MaterialCardView

class UploadMedicalDocumentActivity : AppCompatActivity() {
    private lateinit var continueButton: CustomButton
    private lateinit var laterButton: CustomButton
    private lateinit var uploadCard: MaterialCardView
    private var selectedFileUri: Uri? = null

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            continueButton.enableButton()
            Toast.makeText(this, "Archivo seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_medical_document)

        continueButton = findViewById(R.id.continueButton)
        laterButton = findViewById(R.id.laterButton)
        uploadCard = findViewById(R.id.uploadCard)

        uploadCard.setOnClickListener {
            openFilePicker()
        }

        continueButton.setOnClickListener {
            handleContinue()
        }

        laterButton.setOnClickListener {
            handleLater()
        }
    }

    private fun openFilePicker() {
        filePickerLauncher.launch("*/*")
    }

    private fun handleContinue() {
        println("Continue button clicked - Medical certificate functionality")
    }

    private fun handleLater() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}