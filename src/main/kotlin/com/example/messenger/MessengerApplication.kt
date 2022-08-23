package com.example.messenger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MessengerApplication

fun main(args: Array<String>) {
	runApplication<MessengerApplication>(*args)
}
