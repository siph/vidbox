package com.vidbox.keys

import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeysService(@Autowired private val keysRepository: KeysRepository) {

    fun setTelegramApiKey(owner: String, telegramApiKey: String): Keys {
        val keys = keysRepository.findById(owner).orElse(Keys(owner = owner))
        keys.telegramApiKey = telegramApiKey
        return keysRepository.save(keys)
    }

    fun getKeys(owner: String): Keys {
        return keysRepository.findById(owner).orElseThrow { NotFoundException("User $owner has no keys defined") }
    }
}
