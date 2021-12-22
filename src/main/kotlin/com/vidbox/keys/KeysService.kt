package com.vidbox.keys

import org.slf4j.LoggerFactory
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KeysService(@Autowired private val keysRepository: KeysRepository) {
    companion object {
        val log = LoggerFactory.getLogger(KeysService::class.java)
    }

    fun setTelegramApiKey(owner: String, telegramApiKey: String): Keys {
        log.debug("set telegram api request for owner: $owner")
        val keys = keysRepository.findById(owner).orElse(Keys(owner = owner))
        keys.telegramApiKey = telegramApiKey
        return keysRepository.save(keys)
    }

    fun getKeys(owner: String): Keys {
        log.debug("get keys request received for owner: $owner")
        return keysRepository.findById(owner).orElseThrow { NotFoundException("User $owner has no keys defined") }
    }
}
