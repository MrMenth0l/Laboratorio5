package com.example.laboratorio5.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.laboratorio5.model.PhotoItem
import com.example.laboratorio5.model.PhotoSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PhotosViewModel : ViewModel() {
    private val _photos = MutableStateFlow<List<PhotoItem>>(emptyList())
    val photos: StateFlow<List<PhotoItem>> = _photos

    /** Agrega una foto seleccionada desde el Photo Picker (URI). */
    fun addFromUri(uri: Uri, name: String) {
        val item = PhotoItem(uri = uri, name = name, source = PhotoSource.PICKER)
        _photos.value = _photos.value + item
    }

    /** Agrega una foto tomada con la cámara (Bitmap). */
    fun addFromBitmap(bitmap: Bitmap, name: String) {
        val item = PhotoItem(bitmap = bitmap, name = name, source = PhotoSource.CAMERA)
        _photos.value = _photos.value + item
    }

    /** Limpia la lista de fotos (opcional para pruebas). */
    fun clear() {
        _photos.value = emptyList()
    }
}