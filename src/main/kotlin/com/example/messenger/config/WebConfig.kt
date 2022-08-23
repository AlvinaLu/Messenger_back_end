package com.example.messenger.config

import com.example.messenger.filters.JWTAuthenticationFilter
import com.example.messenger.filters.JWTLoginFilter
import com.example.messenger.services.AppUserDetailsService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig(val userDetailsService: AppUserDetailsService) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            //.antMatchers("/").permitAll()
            .antMatchers(HttpMethod.POST, "/users/registrations").permitAll()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
            .and()
            // Filter the /login requests
            .addFilterBefore(JWTLoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter::class.java)
            // Filter other requests to check the presence of JWT in header
            .addFilterBefore(JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(BCryptPasswordEncoder())
    }
}