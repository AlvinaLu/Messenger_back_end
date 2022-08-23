package com.example.messenger.services

import com.example.messenger.exceptions.ConversationIdInvalidException
import com.example.messenger.models.Conversation
import com.example.messenger.models.Message
import com.example.messenger.models.User
import com.example.messenger.repositories.ConversationRepository
import com.example.messenger.repositories.MessageRepository
import org.springframework.stereotype.Service

/**
 * @author Iyanu Adelekan on 13/10/2017.
 */
@Service
class ConversationServiceImpl(val messageRepository: MessageRepository, val repository: ConversationRepository) : ConversationService {

    override fun createConversation(userA: User, userB: User): Conversation {
        val conversation = Conversation(userA, userB)
        repository.save(conversation)
        return conversation
    }

    override fun conversationExists(userA: User, userB: User): Boolean {
        return if (repository.findBySenderIdAndRecipientId(userA.id, userB.id) != null)
            true
        else repository.findBySenderIdAndRecipientId(userB.id, userA.id) != null
    }

    override fun getConversation(userA: User, userB: User): Conversation? {
        return when {
            repository.findBySenderIdAndRecipientId(userA.id, userB.id) != null ->
                repository.findBySenderIdAndRecipientId(userA.id, userB.id)
            repository.findBySenderIdAndRecipientId(userB.id, userA.id) != null ->
                repository.findBySenderIdAndRecipientId(userB.id, userA.id)
            else -> null
        }

    }

    override fun retrieveThread(conversationId: Long, messageId: Long,  getId: Long): Conversation {
        val conversation = repository.findById(conversationId)

        if (conversation.isPresent) {
            val converse = conversation.get()
            val messagesTmp : ArrayList<Message> = ArrayList()

            converse.messages?.forEach{ message ->
                if(message.unread == getId){
                    message.unread = -1
                    messageRepository.save(message)
                }
                if(message.id > messageId){
                    messagesTmp.add(message)
                }

            }
            converse.messages = messagesTmp
            return converse
        }
        throw ConversationIdInvalidException("Invalid conversation id $conversationId")
    }

    override fun listUserConversations(userId: Long): ArrayList<Conversation> {
        val conversationList: ArrayList<Conversation> = ArrayList()
        conversationList.addAll(repository.findByRecipientId(userId))
        conversationList.addAll(repository.findBySenderId(userId))
        return conversationList
    }

    override fun nameSecondParty(conversation: Conversation, userId: Long): String {
        return if (conversation.sender?.id == userId) {
            conversation.recipient?.username as String
        } else {
            conversation.sender?.username as String
        }
    }

    override fun urlSecondParty(conversation: Conversation, userId: Long): String {
        return if (conversation.sender?.id == userId) {
            conversation.recipient?.imgUrl as String
        } else {
            conversation.sender?.imgUrl as String
        }
    }

    override fun idSecondParty(conversation: Conversation, userId: Long): Long {
        return if (conversation.sender?.id == userId) {
            conversation.recipient?.id as Long
        } else {
            conversation.sender?.id as Long
        }
    }

    override fun secondPartyLastOnline(conversation: Conversation, userId: Long): String {
        return if (conversation.sender?.id == userId) {
            conversation.recipient?.lastOnline.toString()
        } else {
            conversation.sender?.lastOnline.toString()
        }
    }
}