package com.example.messenger.services

import com.example.messenger.controllers.ConversationController
import com.example.messenger.exceptions.MessageDoesntNotExist
import com.example.messenger.exceptions.MessageEmptyException
import com.example.messenger.exceptions.MessageRecipientInvalidException
import com.example.messenger.helpers.objects.TYPE_OF_MESSAGE
import com.example.messenger.models.Conversation
import com.example.messenger.models.Message
import com.example.messenger.models.User
import com.example.messenger.repositories.ConversationRepository
import com.example.messenger.repositories.MessageRepository
import com.example.messenger.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.persistence.Column

/**
 * @author Iyanu Adelekan on 13/10/2017.
 */
@Service
class MessageServiceImpl(
    val repository: MessageRepository, val conversationRepository: ConversationRepository,
    val conversationService: ConversationService, val userRepository: UserRepository
) :
    MessageService {

    companion object {
        private val LOG = LoggerFactory.getLogger(ConversationController::class.java)
    }

    @Throws(MessageDoesntNotExist::class)
    override fun readMessage(userId: Long, id: Long) {
        val optional = repository.findById(id)

        if (optional.isPresent) {
            if (userId == optional.get().unread) {
                optional.get().unread = -1L
                repository.save(optional.get())
            }
        } else {
            throw MessageDoesntNotExist()
        }
    }


    @Throws(MessageEmptyException::class, MessageRecipientInvalidException::class)
    override fun sendMessage(
        sender: User,
        recipientId: Long,
        messageText: String,
        messageType: TYPE_OF_MESSAGE,
        url: String,
        latitude: String,
        longitude: String
    ): Message {
        val optional = userRepository.findById(recipientId)

        if (optional.isPresent) {
            val recipient = optional.get()
            sender.lastOnline = LocalDateTime.now()
            userRepository.save(sender)

            if (messageText.isNotEmpty()) {
                val conversation: Conversation = if (
                    conversationService.conversationExists(sender, recipient)) {
                    conversationService.getConversation(sender, recipient) as Conversation
                } else {
                    conversationService.createConversation(sender, recipient)
                }
                val message = Message(
                    sender,
                    recipient,
                    body = messageText,
                    conversation = conversation,
                    unread = recipientId,
                    type = messageType,
                    url = url,
                    latitude = latitude,
                    longitude = longitude
                )
                repository.save(message)
                return message
            }
        } else {
            throw MessageRecipientInvalidException("The recipient id '$recipientId' is invalid.")
        }
        throw MessageEmptyException()
    }
}