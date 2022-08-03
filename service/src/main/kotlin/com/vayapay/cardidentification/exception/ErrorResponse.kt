package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ErrorResponse(
    val errorDescription: String,
    val errorDate: LocalDateTime
)

fun createErrorResponse(
    httpStatus : HttpStatus,
    errorResponse: ErrorResponse) = ResponseEntity
    .status(httpStatus)
    .contentType(MediaType.APPLICATION_PROBLEM_JSON)
    .body(errorResponse)