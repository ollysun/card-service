package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class CardIdentificationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleBadRequestException(ex: BadRequestException) = createErrorResponse(
        HttpStatus.BAD_REQUEST,
        ex.message!!
    )

    @ExceptionHandler
    fun handleExceptionInternal(ex: CardIdentificationException) = createErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.message!!
    )
}