package com.vidbox.repository

import com.vidbox.model.File
import org.springframework.content.commons.repository.ContentStore


interface FileContentStore: ContentStore<File, String> {
}
