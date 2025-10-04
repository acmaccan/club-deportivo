package com.example.club_deportivo.helpers

import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

/**
 * Utilidades para manejo y validación de archivos
 */
object FileUtils {
    
    /** Tipos de archivo permitidos para documentos */
    private val ALLOWED_MEDICAL_DOCUMENT_TYPES = setOf(
        "application/pdf",
        "image/jpeg",
        "image/jpg", 
        "image/png"
    )
    
    /**
     * Convierte bytes a formato legible (B, KB, MB, GB, TB)
     * @param sizeBytes Tamaño en bytes
     * @return Cadena formateada con unidad correspondiente
     */
    fun formatFileSize(sizeBytes: Long): String {
        if (sizeBytes <= 0) return "0 KB"
        
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(sizeBytes.toDouble()) / log10(1024.0)).toInt()
        
        return String.format(Locale.ROOT, "%.1f %s", 
            sizeBytes / 1024.0.pow(digitGroups.toDouble()), 
            units[digitGroups])
    }
    
    /**
     * Valida si el tipo MIME del archivo es permitido para documentos
     * @param mimeType Tipo MIME del archivo
     * @param onValid Callback ejecutado si el tipo es válido
     * @param onInvalid Callback ejecutado si el tipo es inválido, recibe el tipo inválido
     */
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