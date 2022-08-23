package com.vayapay.cardIdentification.messages

abstract class Response {
    abstract val errorCode: String
    abstract val errorMessage: String?
}