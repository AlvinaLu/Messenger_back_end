package com.example.messenger.helpers.objects

import javax.persistence.Column

data class UserVO(
    val id: Long,
    val username: String,
    val phoneNumber: String,
    val status: String,
    val createdAt: String,
    val imgUrl: String,
    val lastOnline: String,
    var notificationToken: String
)

data class UserListVO(
    val users: List<UserVO>
)

data class MessageVO(
    val id: Long,
    val senderId: Long?,
    val recipientId: Long?,
    val conversationId: Long?,
    val body: String?,
    val createdAt: String,
    val unread: Long,
    var type: TYPE_OF_MESSAGE,
    var url: String,
    val latitude: String,
    val longitude: String

)

enum class TYPE_OF_MESSAGE{TEXT, DOCUMENT, IMAGE, LOCATION}

data class ConversationVO(
    val conversationId: Long,
    val secondPartyUsername: String,
    val secondPartyId: Long,
    val imgUrl: String,
    val lastOnline: String,
    val messages:  List<MessageVO>,
    val unread: Int,
)

data class ConversationListVO(
    val conversations: List<ConversationVO>
)

data class PushNotificationResponse(
    var status: Int = 0, var message: String
)