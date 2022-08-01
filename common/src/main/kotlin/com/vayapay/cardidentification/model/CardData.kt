package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable

@Serializable
data class CardData(val pan: String,
                    val expirationDate: String)
