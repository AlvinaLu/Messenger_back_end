package com.example.messenger.services

import com.example.messenger.controllers.MessageController
import com.example.messenger.controllers.PushNotificationController
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class PushNotificationService(val fcmService: FCMService) {
    val logger = LoggerFactory.getLogger(PushNotificationService::class.java)
    fun sendPushNotificationToToken(request: MessageController.PushNotificationRequest) {
        try {
            fcmService.sendMessageToToken(request!!)
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }
}