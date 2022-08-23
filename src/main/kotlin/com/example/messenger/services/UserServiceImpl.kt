package com.example.messenger.services

import com.example.messenger.exceptions.*
import com.example.messenger.models.User
import com.example.messenger.repositories.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class UserServiceImpl(val repository: UserRepository) : UserService {
    @Throws(UsernameUnavailableException::class)
    override fun attemptRegistration(userDetails: User): User {
        if(userDetails.username.length > 20){
            throw UsernameUnavailableException("The username ${userDetails.username} is too long.")
        }
        if (!usernameExists(userDetails.username)) {
            val user = User()
            user.username = userDetails.username
            user.phoneNumber = userDetails.phoneNumber
            user.password = userDetails.password
            repository.save(user)
            obscurePassword(user)
            return user
        }
        throw UsernameUnavailableException("The username ${userDetails.username} is unavailable.")
    }

    @Throws(UserStatusEmptyException::class)
    fun updateUserStatus(currentUser: User, status: String): User {
        if (status.isNotEmpty()) {
            currentUser.status = status
            currentUser.lastOnline = LocalDateTime.now()
            repository.save(currentUser)
            return currentUser
        }else{
            throw UserStatusEmptyException()
        }

    }

    @Throws(UserStatusEmptyException::class)
    override fun updateUserUrl(currentUser: User, url: String): User {
        if (url.isNotEmpty()) {
            currentUser.imgUrl = url
            currentUser.lastOnline = LocalDateTime.now()
            repository.save(currentUser)
            return currentUser
        }else{
            throw UserUrlEmptyException()
        }
    }

    @Throws(UserTokenEmptyException::class)
    override fun updateUserToken(currentUser: User, token: String): User {
        if (token.isNotEmpty()) {
            currentUser.notificationToken = token
            currentUser.lastOnline = LocalDateTime.now()
            repository.save(currentUser)
            return currentUser
        }else {
            throw UserTokenEmptyException()
        }
    }


    override fun updateUserOnline(currentUser: User): User {
        currentUser.lastOnline = LocalDateTime.now()
        repository.save(currentUser)
        return currentUser
    }

    override fun listUsers(currentUser: User): List<User> {
        return repository.findAll().mapTo(ArrayList(), { it })
            .filter { it != currentUser }
    }

    override fun retrieveUserData(username: String): User? {
        val user = repository.findByUsername(username)
        obscurePassword(user)
        return user
    }

    @Throws(InvalidUserIdException::class)
    override fun retrieveUserData(id: Long): User {
        val userOptional = repository.findById(id)

        if (userOptional.isPresent) {
            val user = userOptional.get()
            obscurePassword(user)
            return user
        }
        throw InvalidUserIdException("A user with an id of '$id' does not exist.")
    }

    override fun usernameExists(username: String): Boolean {
        return repository.findByUsername(username) != null
    }

    private fun obscurePassword(user: User?) {
        user?.password = "XXX XXXX XXX"
    }
}