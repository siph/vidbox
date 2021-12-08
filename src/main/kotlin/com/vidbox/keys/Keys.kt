package com.vidbox.keys

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "keys")
class Keys(@Id val owner: String,
           var telegramApiKey: String? = null)

