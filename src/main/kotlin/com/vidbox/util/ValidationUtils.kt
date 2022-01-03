package com.vidbox.util

import com.vidbox.album.Album
import com.vidbox.file.File
import internal.org.springframework.content.rest.controllers.BadRequestException

class ValidationUtils

fun validateOwnership(owner: String, file: File) {
    if (!owner.equals(file.owner)) {
        throw BadRequestException("owner: $owner is not the owner of this file")
    }
}

fun validateOwnership(owner: String, album: Album) {
    if (!owner.equals(album.owner)) {
        throw BadRequestException("owner: $owner is not the owner of this file")
    }
}
