package com.example.messenger.repositories

import com.example.messenger.models.Message
import org.springframework.data.repository.CrudRepository


interface MessageRepository : CrudRepository<Message, Long> {
    fun findByConversationId(conversationId: Long): List<Message>
}