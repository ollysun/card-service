package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@RestControllerAdvice
class CardIdentificationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleGenericException(ex: CardIdentificationException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        ErrorResponse(
            errorDescription = ex.message,
            errorCode = HttpStatus.BAD_REQUEST.value()
        )
    )

    @ExceptionHandler
    fun handleExceptionInternal(ex: Exception) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        ErrorResponse(
            errorDescription = ex.message,
            errorCode = HttpStatus.BAD_REQUEST.value()
        )
    )
}