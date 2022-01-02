package com.vidbox.util

import com.vidbox.album.Album
import com.vidbox.file.File

class ValidationUtils

fun validateOwnership(owner: String, file: File): Boolean {
    return owner.equals(file.owner)
}

fun validateOwnership(owner: String, album: Album): Boolean {
    return owner.equals(album.owner)
}
