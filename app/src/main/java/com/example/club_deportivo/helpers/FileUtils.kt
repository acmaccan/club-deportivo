package com.example.club_deportivo.helpers

import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

object FileUtils {
    
    private val ALLOWED_MEDICAL_DOCUMENT_TYPES = setOf(
        "application/pdf",
        "image/jpeg",
        "image/jpg", 
        "image/png"
    )
    
    fun formatFileSize(sizeBytes: Long): String {
        if (sizeBytes <= 0) return "0 KB"
        
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeBytes.toDouble()) / log10(1024.0)).toInt()
        
        return String.format(Locale.ROOT, "%.1f %s", 
            sizeBytes / 1024.0.pow(digitGroups.toDouble()), 
            units[digitGroups])
    }
    
    fun validateFileType(
        mimeType: String?,
        onValid: () -> Unit,
        onInvalid: (invalidType: String) -> Unit
    ) {
        if (mimeType != null && mimeType in ALLOWED_MEDICAL_DOCUMENT_TYPES) {
            onValid()
        } else {
            onInvalid(mimeType ?: "unknown")
        }
    }
}