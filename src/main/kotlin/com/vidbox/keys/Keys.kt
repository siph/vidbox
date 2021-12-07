package com.vidbox.keys

import javax.persistence.Id

class Keys(@Id val owner: String,
           var telegramApiKey: String? = null)

