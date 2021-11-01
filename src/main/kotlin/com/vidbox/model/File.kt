package com.vidbox.model

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class File(@Id @GeneratedValue(strategy = GenerationType.AUTO) private val id: Long,
           private val name: String,
           private val created: Date = Date(),
           private val summary: String,
           @ContentId private val contentId: String,
           @ContentLength private val contentLength: Long,
           private val mimeType: String = "text/plain") {

}
