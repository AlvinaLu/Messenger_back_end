package com.example.messenger.services

import com.example.messenger.models.Conversation
import com.example.messenger.models.User

/**
 * @author Iyanu Adelekan on 18/10/2017.
 */
interface ConversationService {

    fun createConversation(userA: User, userB: User): Conversation

    fun conversationExists(userA: User, userB: User): Boolean

    fun getConversation(userA: User, userB: User): Conversation?

    fun retrieveThread(conversationId: Long, messageId: Long, getId: Long): Conversation

    fun listUserConversations(userId: Long): List<Conversation>

    fun nameSecondParty(conversation: Conversation, userId: Long): String

    fun urlSecondParty(conversation: Conversation, userId: Long): String

    fun secondPartyLastOnline(conversation: Conversation, userId: Long): String

    fun idSecondParty(conversation: Conversation, userId: Long): Long
}