package com.vidbox.repository

import com.vidbox.model.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path = "files", collectionResourceRel = "files")
interface FileRepository: JpaRepository<File, Long>
{}
