package com.example.messenger.exceptions

import com.example.messenger.constants.ResponseConstants
import org.springframework.http.HttpStatus


open class ControllerException(message: String, val code: String, val status: HttpStatus = HttpStatus.UNPROCESSABLE_ENTITY) : RuntimeException(message)

class InvalidTokenException(message: String) : ControllerException(message, ResponseConstants.USER_TOKEN_IS_NOT_VALID.value)

class UsernameUnavailableException(message: String) : ControllerException(message, ResponseConstants.USERNAME_UNAVAILABLE.value)

class InvalidUserIdException(message: String) : ControllerException(message, ResponseConstants.INVALID_USER_ID.value, HttpStatus.BAD_REQUEST)

class MessageEmptyException(message: String = "A message cannot be empty.") : ControllerException(message, ResponseConstants.MESSAGE_EMPTY.value)

class MessageDoesntNotExist(message: String = "A message doesn't exist.") : ControllerException(message, ResponseConstants.MESSAGE_DOESNT_EXIST.value)

class MessageRecipientInvalidException(message: String) : ControllerException(message, ResponseConstants.MESSAGE_RECIPIENT_INVALID.value)

class ConversationIdInvalidException(message: String) : ControllerException(message, ResponseConstants.CONVERSATION_INVALID.value)

class UserDeactivatedException(message: String) :ControllerException(message, ResponseConstants.ACCOUNT_DEACTIVATED.value, HttpStatus.UNAUTHORIZED)

class UserStatusEmptyException(message: String = "A user's status cannot be empty") : ControllerException(message, ResponseConstants.EMPTY_STATUS.value)

class UserUrlEmptyException(message: String = "A user's imageUrl cannot be empty") : ControllerException(message, ResponseConstants.EMPTY_URL.value)

class UserTokenEmptyException(message: String = "A user's token cannot be empty") : ControllerException(message, ResponseConstants.EMPTY_TOKEN.value)