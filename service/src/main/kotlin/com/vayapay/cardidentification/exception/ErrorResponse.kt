package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity


fun createErrorResponse(
    httpStatus : HttpStatus,
    msg: String) = ResponseEntity
    .status(httpStatus)
    .contentType(MediaType.APPLICATION_PROBLEM_JSON)
    .body(msg)