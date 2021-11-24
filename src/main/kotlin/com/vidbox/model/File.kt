package com.vidbox.model

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class File(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
           var name: String?,
           var created: Date = Date(),
           val owner: String,
           @ContentId var contentId: String? = null,
           @ContentLength var contentLength: Long? = null,
           var mimeType: String = "text/plain")
