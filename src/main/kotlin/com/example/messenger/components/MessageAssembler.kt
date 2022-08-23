package com.example.messenger.components

import com.example.messenger.helpers.objects.MessageVO
import com.example.messenger.models.Message
import org.springframework.stereotype.Component

@Component
class MessageAssembler {
    fun toMessageVO(message: Message): MessageVO {
        return MessageVO(message.id, message.sender?.id,
                message.recipient?.id, message.conversation?.id,
                message.body, message.createdAt.toString(), message.unread, message.type, message.url, message.latitude.toString(), message.longitude.toString())
    }
}