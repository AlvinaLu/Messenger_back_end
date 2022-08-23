package com.example.messenger.models

import com.example.messenger.helpers.objects.TYPE_OF_MESSAGE
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "`message`")
class Message(
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val sender: User? = null,
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    val recipient: User? = null,
    var body: String? = "",
    @ManyToOne(optional = false)
    @JoinColumn(name = "conversation_id", referencedColumnName = "id")
    var conversation: Conversation? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,
    @DateTimeFormat
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column
    var unread: Long = -1L,
    @Column
    var type: TYPE_OF_MESSAGE = TYPE_OF_MESSAGE.TEXT,
    @Column
    var url: String = "",
    @Column
    val latitude: String = "",
    @Column
    val longitude: String = ""
)