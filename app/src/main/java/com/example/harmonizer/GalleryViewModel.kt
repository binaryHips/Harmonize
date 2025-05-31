package com.example.harmonizer

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@kotlinx.serialization.Serializable
data class PhotoItem(
    val id: Int,
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
            PhotoItem(1,"https://picsum.photos/300?random=1", "Sunset", "2025-05-10"),
            PhotoItem(2,"https://picsum.photos/300?random=2", "Mountains", "2025-05-09"),
            PhotoItem(3,"https://picsum.photos/300?random=3", "Office", "2025-05-08"),
            PhotoItem(4,"https://picsum.photos/300?random=4", "Forest", "2025-05-07"),
            PhotoItem(5,"https://picsum.photos/300?random=5", "City", "2025-05-06"),
            PhotoItem(6,"https://picsum.photos/300?random=6", "Countryside", "2025-05-06"),

            )

    }

    fun deletePhotoById(id: Int) {
        _photos.value = _photos.value.filterNot { it.id == id }
    }
}


