package com.pickdsm.pickserverspring.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.pickdsm.pickserverspring.global.filter.ExceptionFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class FilterConfig(
    private val objectMapper: ObjectMapper
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(builder: HttpSecurity) {
        builder.addFilterBefore(AuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        builder.addFilterBefore(ExceptionFilter(objectMapper), AuthenticationFilter::class.java)
    }
}
