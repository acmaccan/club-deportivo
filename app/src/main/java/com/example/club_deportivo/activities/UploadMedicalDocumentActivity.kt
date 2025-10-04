package com.example.club_deportivo.activities

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.club_deportivo.R
import com.example.club_deportivo.helpers.FileUtils
import com.example.club_deportivo.ui.CustomButton
import com.google.android.material.card.MaterialCardView

class UploadMedicalDocumentActivity : AppCompatActivity() {
    private lateinit var continueButton: CustomButton
    private lateinit var laterButton: CustomButton
    private lateinit var uploadCard: MaterialCardView
    private lateinit var uploadStateLayout: LinearLayout
    private lateinit var fileSelectedLayout: LinearLayout
    private lateinit var fileNameText: TextView
    private lateinit var fileSizeText: TextView
    private lateinit var removeFileButton: ImageView
    private var selectedFileUri: Uri? = null

    companion object {
        private const val TAG = "UploadMedicalDocument"
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            showFileInfo(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_medical_document)

        continueButton = findViewById(R.id.continueButton)
        laterButton = findViewById(R.id.laterButton)
        uploadCard = findViewById(R.id.uploadCard)
        uploadStateLayout = findViewById(R.id.uploadStateLayout)
        fileSelectedLayout = findViewById(R.id.fileSelectedLayout)
        fileNameText = findViewById(R.id.fileNameText)
        fileSizeText = findViewById(R.id.fileSizeText)
        removeFileButton = findViewById(R.id.removeFileButton)

        uploadCard.setOnClickListener {
            openFilePicker()
        }

        continueButton.setOnClickListener {
            handleContinue()
        }

        laterButton.setOnClickListener {
            handleLater()
        }

        removeFileButton.setOnClickListener {
            removeFile()
        }
    }

    private fun openFilePicker() {
        filePickerLauncher.launch("*/*")
    }

    private fun showFileInfo(uri: Uri) {
        // Validate file type
        val mimeType = contentResolver.getType(uri)
        
        FileUtils.validateFileType(
            mimeType = mimeType,
            onValid = {
                // File type is valid, show file info and enable button
                val fileInfo = getFileInfo(uri)
                showFileSelected(fileInfo.first, fileInfo.second)
                continueButton.enableButton()
                Toast.makeText(this,
                    getString(R.string.upload_medical_document_successful_selected_file_label), Toast.LENGTH_SHORT).show()
            },
            onInvalid = { invalidType ->
                Toast.makeText(
                    this, 
                    getString(R.string.upload_medical_document_error_invalid_file_type), 
                    Toast.LENGTH_LONG
                ).show()
                resetToUploadState()
            }
        )
    }

    private fun removeFile() {
        resetToUploadState()
    }
    
    private fun resetToUploadState() {
        selectedFileUri = null
        continueButton.disableButton()
        uploadStateLayout.visibility = View.VISIBLE
        fileSelectedLayout.visibility = View.GONE
    }
    
    private fun showFileSelected(fileName: String, fileSize: String) {
        fileNameText.text = fileName
        fileSizeText.text = fileSize
        uploadStateLayout.visibility = View.GONE
        fileSelectedLayout.visibility = View.VISIBLE
    }

    private fun getFileInfo(uri: Uri): Pair<String, String> {
        var fileName = getString(R.string.upload_medical_document_default_file_name)
        var fileSize = getString(R.string.upload_medical_document_default_file_size)
        
        try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex) ?: fileName
                    }
                    
                    if (sizeIndex != -1) {
                        val sizeBytes = cursor.getLong(sizeIndex)
                        fileSize = FileUtils.formatFileSize(sizeBytes)
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception reading file info", e)
            Toast.makeText(this, getString(R.string.upload_medical_document_error_permission), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error reading file info", e)
            Toast.makeText(this, getString(R.string.upload_medical_document_error_reading_file), Toast.LENGTH_SHORT).show()
        }
        
        return Pair(fileName, fileSize)
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