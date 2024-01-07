package com.pickdsm.pickserverspring.common.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

data class ErrorResponse(
    val status: Int,
    val message: String,
)

data class ValidationErrorResponse(
    val status: Int,
    val fieldError: Map<String, String?>,
)

fun ErrorProperty.of() = ErrorResponse(
    this.status(),
    this.message(),
)

fun BindingResult.of(): ValidationErrorResponse {
    val errorMap = HashMap<String, String?>()

    for (error: FieldError in this.fieldErrors) {
        errorMap[error.field] = error.defaultMessage
    }

    return ValidationErrorResponse(
        status = GlobalErrorCode.BAD_REQUEST.status(),
        fieldError = errorMap,
    )
}

fun DataIntegrityViolationException.of() = ErrorResponse(
    status = GlobalErrorCode.BAD_REQUEST.status(),
    message = this.message.toString(),
)

fun IllegalArgumentException.of() = ErrorResponse(
    status = GlobalErrorCode.BAD_REQUEST.status(),
    message = this.message.toString(),
)
