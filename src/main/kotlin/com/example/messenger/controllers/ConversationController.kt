package com.example.messenger.controllers

import com.example.messenger.components.ConversationAssembler
import com.example.messenger.helpers.objects.ConversationListVO
import com.example.messenger.helpers.objects.ConversationVO
import com.example.messenger.models.User
import com.example.messenger.repositories.UserRepository
import com.example.messenger.services.ConversationServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/conversations")
class ConversationController(val conversationService: ConversationServiceImpl,
                             val conversationAssembler: ConversationAssembler,
                             val userRepository: UserRepository) {

    companion object {
        private val LOG = LoggerFactory.getLogger(ConversationController::class.java)
    }

    @GetMapping
    fun list(request: HttpServletRequest): ResponseEntity<ConversationListVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        LOG.info("Username: ${user?.username} UserId: ${user?.id} get request ConversationListVO")
        val conversations = conversationService.listUserConversations(user.id)
        val response = ResponseEntity.ok(conversationAssembler.toConversationListVO(conversations, user.id))
        LOG.info("\"Username: ${user?.username} UserId: ${user?.id} response ConversationListVO ${response?.body?.toString()}")
        return response
    }


    @GetMapping
    @RequestMapping("/{conversation_id}/{last_message_id}")
    fun show(@PathVariable(name = "conversation_id") conversationId: Long, @PathVariable(name = "last_message_id") messageId: Long, request: HttpServletRequest): ResponseEntity<ConversationVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        LOG.info("Username: ${user.username} UserId: ${user.id} get request ConversationListVO")
        val conversationThread = conversationService.retrieveThread(conversationId, messageId, user.id)
        val response = ResponseEntity.ok(conversationAssembler.toConversationVO(conversationThread, user.id))
        LOG.info("\"Username: ${user.username} UserId: ${user.id} response ConversationListVO ${response.body.toString()}")
        return response
    }
}