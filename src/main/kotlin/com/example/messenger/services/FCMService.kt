package com.example.messenger.services

import com.example.messenger.controllers.MessageController
import com.example.messenger.helpers.objects.TYPE_OF_MESSAGE
import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.time.Duration;


@Service
class FCMService {
    val logger = LoggerFactory.getLogger(FCMService::class.java)
    @Throws(InterruptedException::class, ExecutionException::class)
    fun sendMessageToToken(request: MessageController.PushNotificationRequest) {
        val message: Message = getPreconfiguredMessageToToken(request)
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonOutput = gson.toJson(message)
        val response = sendAndGetResponse(message)
        logger.info("Sent message to token. Device token: " + request.token + ", " + response + " msg " + jsonOutput)
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun sendAndGetResponse(message: Message): String {
        return FirebaseMessaging.getInstance().sendAsync(message).get()
    }

    private fun getAndroidConfig(topic: String?): AndroidConfig {
        return AndroidConfig.builder()
            .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(
                AndroidNotification.builder()
                    .setTag(topic).build()
            ).build()
    }

    private fun getApnsConfig(topic: String?): ApnsConfig {
        return ApnsConfig.builder()
            .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build()
    }

    private fun getPreconfiguredMessageToToken(request: MessageController.PushNotificationRequest): Message {
        val response = getPreconfiguredMessageBuilder(request).setToken(request.token)
            .build()
        return response
    }

    private fun getPreconfiguredMessageWithoutData(request: MessageController.PushNotificationRequest): Message {
        return getPreconfiguredMessageBuilder(request).setTopic(request.topic)
            .build()
    }

    private fun getPreconfiguredMessageWithData(data: Map<String, String>, request: MessageController.PushNotificationRequest): Message {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.token)
            .build()
    }

    private fun getPreconfiguredMessageBuilder(request: MessageController.PushNotificationRequest): Message.Builder {
        val androidConfig = getAndroidConfig(request.topic)
        val apnsConfig = getApnsConfig(request.topic)
        val map : Map<String, String> = mapOf(
            "ID" to "${request.messageId}",
            "SENDER_ID" to "${request.senderId}",
            "RECIPIENT_ID" to "${request.recipientId}",
            "SENDER_USERNAME" to "${request.title}" ,
            "SENDER_MESSAGE" to "${request.message}",
            "SENDER_URL" to "${request.senderImgUrl}",
            "LAST_ONLINE" to "${request.senderLastOnline}",
            "MESSAGE_TIME" to "${request.messageTime}",
            "CONVERSATION_ID" to "${request.conversationId}",
            "UNREAD" to "${request.unread}",
            "TYPE" to "${TYPE_OF_MESSAGE.valueOf(request.type)}",
            "URL" to "${request.url}",
            "LATITUDE" to "${request.latitude}",
            "LONGITUDE" to "${request.longitude}"
        )
        return Message.builder().putAllData(map)
            .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
                Notification.builder().setTitle(request.title).setBody(request.message).setImage("ic_stat_svg_4").build()
            )
    }
}