package com.vayapay.cardidentification.model

import com.vayapay.message.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CardIdResponse(
    val cardScheme: String,
    @Serializable(with = UUIDSerializer::class)
    val paymentCardReference: UUID,
    @Serializable(with = UUIDSerializer::class)
    val travellerAccountReference: UUID? = null
)