package com.example.messenger.filters

import com.example.messenger.components.AccountValidityInterceptor
import com.example.messenger.security.AccountCredentials
import com.example.messenger.services.TokenAuthenticationService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTLoginFilter(url: String, authManager: AuthenticationManager) :
    AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {

    init {
        authenticationManager = authManager
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(JWTLoginFilter::class.java)
    }

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest, res: HttpServletResponse
    ): Authentication {
        val credentials = ObjectMapper()
            .readValue(req.inputStream, AccountCredentials::class.java)

        LOG.info("/login ${credentials.username} ${credentials.password}")

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                credentials.username,
                credentials.password,
                emptyList()
            )
        )
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse, chain: FilterChain,
        auth: Authentication
    ) {
        LOG.info("/successfulAuthentication:  ${req.queryString?.toString()} ${auth.credentials?.toString()}")
        TokenAuthenticationService
            .addAuthentication(res, auth.name)
    }
}