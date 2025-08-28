package com.example.laboratorio5.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

/**
 * Utilidad para obtener el nombre de archivo (DISPLAY_NAME) de un Uri.
 * Usa ContentResolver cuando el esquema es `content://` y cae a heurísticas
 * para otros esquemas (`file://` o rutas arbitrarias).
 */
object UriResolver {
    /**
     * Retorna un nombre de archivo amigable para mostrar en la UI.
     * Nunca lanza excepción; si falla, retorna "Imagen" como fallback.
     */
    fun getDisplayName(context: Context, uri: Uri): String {
        return try {
            when (uri.scheme?.lowercase()) {
                "content" -> queryDisplayName(context, uri) ?: fallbackName(uri)
                "file" -> File(uri.path.orEmpty()).name.ifBlank { fallbackName(uri) }
                else -> fallbackName(uri)
            }
        } catch (_: Exception) {
            fallbackName(uri)
        }
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1 && cursor.moveToFirst()) {
                return cursor.getString(index)
            }
        }
        return null
    }

    private fun fallbackName(uri: Uri): String {
        return uri.lastPathSegment
            ?.substringAfterLast('/')
            ?.takeIf { it.isNotBlank() }
            ?: "Imagen"
    }
}