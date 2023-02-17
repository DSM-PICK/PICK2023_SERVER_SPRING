package com.pickdsm.pickserverspring.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
) {

    @Bean
    @Throws(Exception::class)
    protected fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .formLogin().disable()
            .csrf().disable()
            .cors().and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .authorizeRequests()

            //.antMatchers("/admin/head").hasAuthority(UserRole.SCH.name) TODO: 권한 설정하기
            .anyRequest().permitAll()

        http
            .apply(FilterConfig(objectMapper))

        return http.build()
    }
}
