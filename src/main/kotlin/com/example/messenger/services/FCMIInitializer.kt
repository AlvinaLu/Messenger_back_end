package com.example.messenger.services

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class FCMIInitializer{
    @Value("\${app.firebase-configuration-file}")
    private lateinit var firebaseConfigPath : String
    val logger = LoggerFactory.getLogger(FCMIInitializer::class.java)
    @PostConstruct
    public fun initialize() {
        try{
            val options =  FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(ClassPathResource(firebaseConfigPath).inputStream)).build()
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options)
                logger.info("Firebaze app have ben initialized")
            }
        }catch (e: Exception){
            logger.error(e.message, e)
        }


    }
}