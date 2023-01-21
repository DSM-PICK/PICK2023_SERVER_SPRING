package com.pickdsm.pickserverspring.global.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val userId: String? = request.getHeader("Request-User-Id")
        val userRole: UserRole? = request.getHeader("Request-User-Role")?.run { UserRole.valueOf(this) }
        val userAuthorities: List<String>? = request.getHeader("Request-User-Authorities")?.run { listOf(this) }

        if (userId == null || userRole == null || userAuthorities == null) {
            filterChain.doFilter(request, response)
            return
        }

        val authorities: MutableCollection<SimpleGrantedAuthority> = ArrayList()
        for (userAuthority in userAuthorities) {
            authorities.add(SimpleGrantedAuthority(userAuthority))
        }

        authorities.add(SimpleGrantedAuthority("ROLE_${userRole.name}"))
        val userDetails: UserDetails = User(userId, "", authorities)
        val authentication: Authentication =
            UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}
