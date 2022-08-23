package com.example.messenger.repositories

import com.example.messenger.models.Conversation
import org.springframework.data.repository.CrudRepository


interface ConversationRepository : CrudRepository<Conversation, Long> {
    fun findBySenderId(id: Long): List<Conversation>

    fun findByRecipientId(id: Long): List<Conversation>

    fun findBySenderIdAndRecipientId(senderId: Long, recipientId: Long): Conversation?
}