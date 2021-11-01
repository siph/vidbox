package com.vidbox.model

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class File(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long?,
           var name: String?,
           var created: Date = Date(),
           var summary: String?,
           @ContentId var contentId: String?,
           @ContentLength var contentLength: Long?,
           var mimeType: String = "text/plain") {

}
