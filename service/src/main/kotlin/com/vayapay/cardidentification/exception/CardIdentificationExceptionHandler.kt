package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class CardIdentificationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleGenericException(ex: CardIdentificationException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        ErrorResponse(
            errorDescription = ex.message!!,
            errorDate = LocalDateTime.now()
        )
    )
}