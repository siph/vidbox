package com.vidbox.repository

import com.vidbox.model.Album
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository: JpaRepository<Album, Long> {
    fun findAllByOwner(owner: String, pageable: Pageable): Page<Album>
}
