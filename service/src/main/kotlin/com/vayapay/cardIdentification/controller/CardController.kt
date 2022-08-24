package com.vayapay.cardIdentification.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardFormData
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.util.SelectOptions
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@Controller
@RequestMapping("/card")
class CardController(val cardService: CardIdentificationService) {
    @GetMapping("")
    suspend fun cardForm(model: Model): String {
        model["options"] = SelectOptions
        model["cardFormData"] = CardFormData()
        return "add-card"
    }

    @RequestMapping("", method = [RequestMethod.POST])
    suspend fun createCard(
        @ModelAttribute cardFormData: CardFormData,
        errors: BindingResult,
        model: Model
    ): String {
        val cardData = CardData(cardFormData.cardNumber, cardFormData.expiryMonth + "/" + cardFormData.expiryYear)
        val cardRequestDto = CardRequestDto("PTO_1", cardData, cardFormData.accountNumber)
        val storeCardResponse = cardService.saveCardStorage(cardRequestDto)
        model["options"] = SelectOptions

        if (storeCardResponse.errorMessage.isNotEmpty())
            model["saveCardError"] = storeCardResponse.errorMessage
        else
            model["response"] = jacksonObjectMapper().writeValueAsString(storeCardResponse)

        return "add-card"
    }
}