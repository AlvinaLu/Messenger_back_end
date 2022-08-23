package com.example.messenger.controllers

import com.example.messenger.components.UserAssembler
import com.example.messenger.helpers.objects.UserListVO
import com.example.messenger.helpers.objects.UserVO
import com.example.messenger.models.User
import com.example.messenger.repositories.UserRepository
import com.example.messenger.services.UserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserServiceImpl,
    val userAssembler: UserAssembler, val userRepository: UserRepository
) {
    companion object {
        private val LOG = LoggerFactory.getLogger(UserController::class.java)
    }

    @PostMapping
    @RequestMapping("/registrations")
    fun create(@Validated @RequestBody userDetails: User): ResponseEntity<UserVO> {
        LOG.info("createUser req: username - ${userDetails.username.toString()} password - ${userDetails.password.toString()}")
        val user = userService.attemptRegistration(userDetails)
        val response = ResponseEntity.ok(userAssembler.toUserVO(user))
        LOG.info("createUser res: ${response.body.toString()}")
        return response
    }

    @GetMapping
    @RequestMapping("/{user_id}")
    fun show(@PathVariable("user_id") userId: Long): ResponseEntity<UserVO> {
        LOG.info("getUser req: userId - ${userId.toString()}")
        val user = userService.retrieveUserData(userId)
        val response = ResponseEntity.ok(userAssembler.toUserVO(user))
        LOG.info("getUser req: ${response.body.toString()}")
        return response
    }

    @GetMapping
    @RequestMapping("/details")
    fun echoDetails(request: HttpServletRequest): ResponseEntity<UserVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        LOG.info("echoDetails username: ${user.username} UserId: ${user.id} ")
        val response = ResponseEntity.ok(userAssembler.toUserVO(user))
        LOG.info("echoDetails  ${response.body.toString()}")
        return response
    }

    @GetMapping
    fun index(request: HttpServletRequest): ResponseEntity<UserListVO> {
        val user = userRepository.findByUsername(request.userPrincipal.name) as User
        val users = userService.listUsers(user)
        return ResponseEntity.ok(userAssembler.toUserListVO(users))
    }

    @PutMapping
    fun update(@RequestBody updateDetails: StatusUpdateRequestObject, request: HttpServletRequest):
            ResponseEntity<UserVO> {
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        userService.updateUserStatus(currentUser as User, updateDetails.status)
        return ResponseEntity.ok(userAssembler.toUserVO(currentUser))
    }

    @PostMapping()
    @RequestMapping("/updateUrl")
    fun updateUrl(@RequestBody updateDetails: UrlUpdateRequestObject, request: HttpServletRequest):
            ResponseEntity<UserVO> {
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        LOG.info("updateUrl username: ${currentUser?.username} UserId: ${currentUser?.id} UserURL: ${currentUser?.imgUrl} ")
        userService.updateUserUrl(currentUser as User, updateDetails.imgUrl)
        val response  = ResponseEntity.ok(userAssembler.toUserVO(currentUser))
        LOG.info("updateUrl ${response.body.toString()}")
        return response
    }

    @PostMapping()
    @RequestMapping("/updateOnline")
    fun updateOnline(request: HttpServletRequest): ResponseEntity<UserVO>{
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        LOG.info("updateOnline username: ${currentUser?.username} UserId: ${currentUser?.id} UserURL: ${currentUser?.imgUrl} ")
        userService.updateUserOnline(currentUser as User)
        val response  = ResponseEntity.ok(userAssembler.toUserVO(currentUser))
        LOG.info("updateUrl ${response.body.toString()}")
        return response
    }

    @PutMapping
    @RequestMapping("/token")
    fun putToken(@RequestBody updateDetails: TokenUpdateRequestObject, request: HttpServletRequest): ResponseEntity<UserVO> {
        val currentUser = userRepository.findByUsername(request.userPrincipal.name)
        LOG.info("updateToken username: ${currentUser?.username} UserId: ${currentUser?.id} ")
        userService.updateUserToken(currentUser as User, updateDetails.notificationToken)
        val response  = ResponseEntity.ok(userAssembler.toUserVO(currentUser))
        LOG.info("updateToken ${response.body.toString()}")
        return response
    }


    data class StatusUpdateRequestObject(val status: String)
    class UrlUpdateRequestObject(val imgUrl : String )
    data class TokenUpdateRequestObject(val notificationToken: String)
}