package com.example.messenger.components

import com.example.messenger.controllers.ConversationController
import com.example.messenger.exceptions.InvalidTokenException
import com.example.messenger.exceptions.UserDeactivatedException
import com.example.messenger.models.User
import com.example.messenger.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccountValidityInterceptor(val userRepository: UserRepository) : HandlerInterceptorAdapter() {

    companion object {
        private val LOG = LoggerFactory.getLogger(AccountValidityInterceptor::class.java)
    }

    @Throws(UserDeactivatedException::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val principal: Principal? = request.userPrincipal

        if (principal != null) {
            val user = userRepository.findByUsername(principal.name) ?: throw InvalidTokenException("Invalid token for ${principal.name}")

            if (user.accountStatus == "deactivated") {
                LOG.error("Username: ${user.username} UserId: ${user.id} UserDeactivatedException: The account of this user has been deactivated")
                throw UserDeactivatedException("The account of this user has been deactivated.")
            }
        }
        return super.preHandle(request, response, handler)
    }
}