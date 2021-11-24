package com.vidbox.repository

import com.vidbox.model.Album
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository: JpaRepository<Album, Long> {
}
