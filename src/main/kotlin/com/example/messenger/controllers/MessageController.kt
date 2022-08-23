package com.example.messenger.controllers

import com.example.messenger.components.MessageAssembler
import com.example.messenger.exceptions.InvalidUserIdException
import com.example.messenger.exceptions.InvalidTokenException
import com.example.messenger.helpers.objects.MessageVO
import com.example.messenger.helpers.objects.PushNotificationResponse
import com.example.messenger.helpers.objects.TYPE_OF_MESSAGE
import com.example.messenger.models.User
import com.example.messenger.repositories.UserRepository
import com.example.messenger.services.MessageServiceImpl
import com.example.messenger.services.PushNotificationService
import com.example.messenger.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.NoSuchElementException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/messages")
class MessageController(
    val messageService: MessageServiceImpl,
    val userRepository: UserRepository,
    val userService: UserService,
    val messageAssembler: MessageAssembler,
    val pushNotificationService: PushNotificationService
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MessageController::class.java)
    }


    @PostMapping("/read")
    fun read(@RequestBody messageRequest: MessageReadRequest, request: HttpServletRequest): ResponseEntity<*> {
        val user = request.userPrincipal
        val sender = userRepository.findByUsername(user.name) as User
        messageService.readMessage(sender.id, messageRequest.messageId)
        return ResponseEntity<Any>(
            PushNotificationResponse(HttpStatus.OK.value(), "Message has been read."),
            HttpStatus.OK
        )
    }

    @PostMapping
    fun create(@RequestBody messageDetails: MessageRequest, request: HttpServletRequest): ResponseEntity<MessageVO> {
        LOG.info("createMessage req : ${messageDetails.message.toString()}")
        val principal = request.userPrincipal
        try {
            val sender = userRepository.findByUsername(principal.name)!!


            val id: Long = messageDetails.recipientId.toLong()

            val recipient = userRepository.findById(id).get()


            val message = messageService.sendMessage(
                sender,
                messageDetails.recipientId,
                messageDetails.message,
                messageDetails.type,
                messageDetails.url,
                messageDetails.latitude,
                messageDetails.longitude
            )
            val response = ResponseEntity.ok(messageAssembler.toMessageVO(message))
            LOG.info("createMessage response : ${response.body.toString()}")

            if (recipient.notificationToken.isNotEmpty()) {
                val notificationRequest = PushNotificationRequest(
                    sender.username,
                    messageDetails.message,
                    "Chatter",
                    recipient.notificationToken,
                    sender.id.toString(),
                    sender.imgUrl,
                    sender.lastOnline.toString(),
                    message.createdAt.toString(),
                    recipient.id.toString(),
                    message.conversation?.id.toString(),
                    message.id.toString(),
                    message.unread.toString(),
                    message.type.toString(),
                    message.url,
                    message.latitude,
                    message.longitude
                )
                pushNotificationService.sendPushNotificationToToken(notificationRequest)
            }
            return ResponseEntity.ok(messageAssembler.toMessageVO(message))
        } catch (e: NoSuchElementException) {
            throw InvalidUserIdException("User with id ${messageDetails.recipientId} doesn't exist!")
        }
    }


    data class MessageRequest(
        val recipientId: Long,
        val message: String,
        val type: TYPE_OF_MESSAGE,
        val url: String,
        val latitude: String,
        val longitude: String
    )

    data class MessageReadRequest(val recipientId: Long, val messageId: Long)
    data class PushNotificationRequest(
        val title: String,
        val message: String,
        val topic: String,
        val token: String,
        val senderId: String,
        val senderImgUrl: String,
        val senderLastOnline: String,
        val messageTime: String,
        val recipientId: String,
        val conversationId: String,
        val messageId: String,
        val unread: String,
        val type: String,
        val url: String,
        val latitude: String,
        val longitude: String
    )
}