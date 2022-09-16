package com.vayapay.cardIdentification.controller

import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardRequestDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid


@RestController
@RequestMapping("/card-identification")
class CardIdentificationController(
        val cardService: CardIdentificationService) {
    @PostMapping("/cards")
    suspend fun cardRegistration(@RequestBody @Valid cardRequestDto: CardRequestDto): StoreAndLinkCardDataResponse {
        return cardService.saveCardStorage(cardRequestDto)
    }
}