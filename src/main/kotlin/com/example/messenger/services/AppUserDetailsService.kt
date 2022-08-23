package com.example.messenger.services

import com.example.messenger.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.ArrayList

/**
 * @author Iyanu Adelekan on 12/10/2017.
 */
@Component
class AppUserDetailsService(val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?:
                throw UsernameNotFoundException("A user with the username $username doesn't exist")

        //val authorities = ArrayList<GrantedAuthority>()
        //user.roles?.forEach { role -> authorities.add(SimpleGrantedAuthority(role.name)) }

        return User(user.username, user.password, ArrayList<GrantedAuthority>())
    }
}