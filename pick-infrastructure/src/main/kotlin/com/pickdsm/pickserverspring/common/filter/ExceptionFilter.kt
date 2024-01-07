package com.pickdsm.pickserverspring.common.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.pickdsm.pickserverspring.common.error.ErrorProperty
import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.common.error.of
import com.pickdsm.pickserverspring.common.exception.InternalServerErrorException
import io.sentry.Sentry
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            when (e.cause) {
                is PickException -> {
                    errorToJson((e.cause as PickException).errorProperty, response)
                }

                is Exception -> {
                    errorToJson(InternalServerErrorException.errorProperty, response)
                    Sentry.captureException(e)
                }
            }
        }
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.apply {
            status = errorProperty.status()
            characterEncoding = StandardCharsets.UTF_8.name()
            contentType = MediaType.APPLICATION_JSON_VALUE
            writer.write(objectMapper.writeValueAsString(errorProperty.of()))
        }
    }
}
