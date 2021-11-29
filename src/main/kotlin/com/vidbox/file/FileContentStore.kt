package com.vidbox.file

import org.springframework.content.commons.repository.ContentStore


interface FileContentStore: ContentStore<File, String> {
}
