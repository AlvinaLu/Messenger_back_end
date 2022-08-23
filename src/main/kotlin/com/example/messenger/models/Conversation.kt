package com.example.messenger.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "`conversation`")
class Conversation(
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    var sender: User? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    var recipient: User? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    @DateTimeFormat
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @OneToMany(mappedBy = "conversation", targetEntity = Message::class)
    var messages: Collection<Message>? = null,
)