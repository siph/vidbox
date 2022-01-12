package com.vidbox.editor

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EditorServiceTests(@Autowired private val editorService: EditorService) {

    @Test
    fun `validate`() {
    }
}
