package com.example.messenger.models

import com.example.messenger.listeners.UserListener
import javax.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@EntityListeners(UserListener::class)
@Table(name = "`user`")
class User(
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    @Column(unique = true)
    @Size(min = 2)
    var username: String = "",
    @Column
    @Size(min = 11)
    @Pattern(regexp = "^\\(?(//d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$")
    var phoneNumber: String = "",
    @Column
    var password: String = "",
    @Column
    var status: String = "",
    @Column
    var imgUrl: String = "",
    @Column
    @Pattern(regexp = "//A(activated|deactivated)\\z")
    var accountStatus: String = "activated",
    @Column
    @DateTimeFormat
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    @DateTimeFormat
    var lastOnline: LocalDateTime = LocalDateTime.now(),
    @Column
    var notificationToken: String = ""
) {
    @OneToMany(mappedBy = "sender", targetEntity = Message::class)
    private var sentMessages: Collection<Message>? = null

    @OneToMany(mappedBy = "recipient", targetEntity = Message::class)
    private var receivedMessages: Collection<Message>? = null

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }


}