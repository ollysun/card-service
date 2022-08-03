package com.vayapay.cardidentification.model

import kotlinx.serialization.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Serializable
data class CardRequestDto(
                          @field: NotBlank(message ="account number is mandatory")
                          val id: String,
                          val cardData : CardData,
                          @field: NotBlank(message ="account number is mandatory")
                          @field: Size(max=11, message = "please enter eleven digit")
                          val bankAccountNumber: String)
