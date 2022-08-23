package com.example.messenger.components

import com.example.messenger.helpers.objects.UserListVO
import com.example.messenger.helpers.objects.UserVO
import com.example.messenger.models.User
import org.springframework.stereotype.Component

@Component
class UserAssembler {

    fun toUserVO(user: User): UserVO {
        return UserVO(user.id, user.username, user.phoneNumber,
                user.status, user.createdAt.toString(), user.imgUrl.toString(), user.lastOnline.toString(), user.notificationToken.toString())
    }

    fun toUserListVO(users: List<User>): UserListVO {
        val userVOList = users.map { toUserVO(it) }

        return  UserListVO(userVOList)
    }
}