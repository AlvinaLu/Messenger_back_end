package com.example.messenger.services

import com.example.messenger.models.User

/**
 * @author Iyanu Adelekan on 18/10/2017.
 */
interface UserService {
    fun attemptRegistration(userDetails: User): User

    fun listUsers(currentUser: User): List<User>

    fun retrieveUserData(username: String): User?

    fun retrieveUserData(id: Long): User?

    fun usernameExists(username: String): Boolean

    fun updateUserUrl(currentUser: User, updateDetails: String): User

    fun updateUserOnline(currentUser: User): User

    fun updateUserToken(currentUser: User, updateDetails: String): User

}