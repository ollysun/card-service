package com.vayapay.cardidentification.messages

abstract class Response {
    abstract val errorCode : String
    abstract val errorMessage : String?
}