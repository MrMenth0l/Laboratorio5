package com.example.laboratorio5.model

import android.graphics.Bitmap
import android.net.Uri
import java.util.UUID

/** Fuente de la imagen: seleccionada desde el Photo Picker o tomada con la cámara. */
enum class PhotoSource { PICKER, CAMERA }

/**
 * Modelo in-memory para una foto mostrada en el grid.
 * Al menos uno entre [uri] o [bitmap] debe ser no nulo.
 */
data class PhotoItem(
    val id: String = UUID.randomUUID().toString(),
    val uri: Uri? = null,
    val bitmap: Bitmap? = null,
    val name: String,
    val source: PhotoSource
) {
    init {
        require(uri != null || bitmap != null) {
            "PhotoItem requires either a non-null uri or bitmap"
        }
    }
}