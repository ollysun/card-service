package com.vayapay.cardidentification.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


class CardIdentificationException(message: String ) : RuntimeException(message)


