package com.vidbox.util

import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile

class TestFileUtils {}

fun getMockFile(): MockMultipartFile {
    return MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "testing".encodeToByteArray())
}
