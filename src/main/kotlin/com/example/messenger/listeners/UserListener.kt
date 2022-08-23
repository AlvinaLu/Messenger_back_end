package com.example.messenger.listeners

import com.example.messenger.models.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.PrePersist

class UserListener {
    @PrePersist
    fun hashPassword(user: User){
        user.password = BCryptPasswordEncoder().encode(user.password)
    }
}