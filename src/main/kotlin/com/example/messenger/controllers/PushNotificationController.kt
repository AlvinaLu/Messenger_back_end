package com.example.messenger.controllers

import com.example.messenger.helpers.objects.PushNotificationResponse
import com.example.messenger.services.PushNotificationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/notification")
class PushNotificationController(pushNotificationService: PushNotificationService) {
    private val pushNotificationService: PushNotificationService

    init {
        this.pushNotificationService = pushNotificationService
    }

    @PostMapping("/token")
    fun sendTokenNotification(@RequestBody request: MessageController.PushNotificationRequest): ResponseEntity<*> {
        pushNotificationService.sendPushNotificationToToken(request)
        return ResponseEntity<Any>(
            PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
            HttpStatus.OK
        )
    }

}