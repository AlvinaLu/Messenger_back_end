package com.example.messenger.repositories

import com.example.messenger.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    override fun findById(id: Long): Optional<User>
}