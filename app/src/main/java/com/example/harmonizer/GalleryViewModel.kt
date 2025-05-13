package com.example.harmonizer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PhotoItem(
    val url: String,
    val title: String,
    val date: String
)

class GalleryViewModel : ViewModel() {
    private val _photos = MutableStateFlow<List<PhotoItem>>(emptyList())
    val photos: StateFlow<List<PhotoItem>> = _photos

    init {
        loadPhotos()
    }

    private fun loadPhotos() {
        _photos.value = listOf(
            PhotoItem("https://picsum.photos/300?random=1", "Sunset", "2025-05-10"),
            PhotoItem("https://picsum.photos/300?random=2", "Mountains", "2025-05-09"),
            PhotoItem("https://picsum.photos/300?random=3", "Beach", "2025-05-08"),
            PhotoItem("https://picsum.photos/300?random=4", "Forest", "2025-05-07"),
            PhotoItem("https://picsum.photos/300?random=5", "City", "2025-05-06")
        )
    }
}
