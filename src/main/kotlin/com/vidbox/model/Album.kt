package com.vidbox.model

import javax.persistence.*

@Entity
class Album(@Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
            @ElementCollection var files: Set<Long>,
            val owner: String)

