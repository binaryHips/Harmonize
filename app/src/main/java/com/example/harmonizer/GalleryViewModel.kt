package com.example.harmonizer

import android.app.Application
import android.content.ContentUris
import android.icu.text.SimpleDateFormat
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
import android.net.Uri
import android.provider.ContactsContract.Contacts.Photo
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import java.sql.Date
import java.util.Locale


data class PhotoItem(
    val id: Int,
    val uri: Uri,
    val title: String,
    val date: String,
    val variants: ArrayList<Uri> = ArrayList<Uri>()
)

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photos = MutableStateFlow<MutableList<PhotoItem>>(ArrayList<PhotoItem>())
    val photos: StateFlow<MutableList<PhotoItem>> = _photos

    init {
        loadLocalImages("Pictures/Harmonizer")
    }
    /*
    private var nextPhotoIndex:Int = 0
    fun getNextImageIndex(): Int{
        return nextPhotoIndex++
    }

    fun addImage(photo: PhotoItem){
        _photos.value.add(photo)
    }
    */
    fun getFromUriOrNull(uri:Uri): PhotoItem?{ // returns the photo object if we already have it here
        return _photos.value.find { it.uri == uri }
    }

    private fun loadLocalImages(folder: String) {
        val context = getApplication<Application>().applicationContext
        val imageList = mutableListOf<PhotoItem>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.RELATIVE_PATH
        )

        // Only get images from the given folder (e.g. "Pictures/MyAppPhotos/")
        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("%$folder%") // e.g., "%Pictures/MyAppPhotos%"

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            var count = 0
            while (cursor.moveToNext() && count < 50) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )

                imageList.add(
                    PhotoItem(
                        id = count + 1,
                        uri = contentUri,
                        title = name,
                        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(date))
                    )
                )
                count++
            }
            //nextPhotoIndex = count+1
        }

        _photos.value = imageList
    }

    fun deletePhotoById(id: Int) {
        _photos.value.removeAll { it.id == id }
    }

    fun refresh() {
        loadLocalImages("Pictures/Harmonizer")
    }

}
