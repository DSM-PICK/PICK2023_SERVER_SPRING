package com.pickdsm.pickserverspring.global.error

import com.pickdsm.pickserverspring.common.error.ErrorProperty
import org.springframework.validation.BindException
import org.springframework.validation.FieldError

data class ErrorResponse(
    val status: Int,
    val message: String
) {

    companion object {
        fun of(errorProperty: ErrorProperty) = ErrorResponse(
            errorProperty.status(),
            errorProperty.message()
        )

        fun of(e: BindException): BindErrorResponse {
            val errorMap = HashMap<String, String?>()

            for (error: FieldError in e.fieldErrors) {
                errorMap[error.field] = error.defaultMessage
            }

            return BindErrorResponse(
                status = GlobalErrorCode.BAD_REQUEST.status(),
                fieldError = errorMap
            )
        }
    }
}

data class BindErrorResponse(
    val status: Int,
    val fieldError: Map<String, String?>
)