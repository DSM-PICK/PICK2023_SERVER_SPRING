package com.pickdsm.pickserverspring.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.pickdsm.pickserverspring.common.error.ErrorProperty
import com.pickdsm.pickserverspring.common.error.PickException
import com.pickdsm.pickserverspring.global.error.ErrorResponse
import com.pickdsm.pickserverspring.global.exception.InternalServerErrorException
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
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: PickException) {
            errorToJson(e.errorProperty, response)
        } catch (e: Exception) {
            when (e.cause) {
                is PickException -> errorToJson((e.cause as PickException).errorProperty, response)
                else -> {
                    errorToJson(InternalServerErrorException.errorProperty, response)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.status = errorProperty.status()
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)))
    }

}