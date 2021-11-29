package com.vidbox.file

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, Long> {
    fun findAllByOwner(owner: String, pageable: Pageable): Page<File>
}
