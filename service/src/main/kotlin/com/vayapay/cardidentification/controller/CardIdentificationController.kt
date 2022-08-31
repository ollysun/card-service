package com.vayapay.cardidentification.controller

import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cardidentification.messages.StoreCardDataResponse
import com.vayapay.cardidentification.model.CardRequestDto
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import javax.validation.Valid


@Controller
@RequestMapping("/card-identification")
class CardIdentificationController(
        val cardService: CardIdentificationService) {

    @PostMapping("/cards")
    fun cardRegistration(@RequestBody @Valid cardRequestDto: CardRequestDto): Mono<StoreCardDataResponse> {
        return cardService.saveCardStorage(cardRequestDto);
    }
}