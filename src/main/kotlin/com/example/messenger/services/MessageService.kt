package com.example.messenger.services

import com.example.messenger.helpers.objects.MessageVO
import com.example.messenger.helpers.objects.TYPE_OF_MESSAGE
import com.example.messenger.models.Message
import com.example.messenger.models.User

/**
 * @author Iyanu Adelekan on 18/10/2017.
 */
interface MessageService {

    fun sendMessage(sender: User, recipientId: Long, messageText: String, messageType: TYPE_OF_MESSAGE, url: String, latitude: String, longitude: String): Message
    fun readMessage(userId: Long, id: Long)
}