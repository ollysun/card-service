package com.vayapay.cardIdentification.controller

import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardFormData
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.util.SelectOptions
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid


@Controller
@RequestMapping("/card")
class CardController(val cardService: CardIdentificationService) {

    @GetMapping("")
    suspend fun addCard(
        @ModelAttribute cardFormData: CardFormData,
        bindingResult: BindingResult,
        model: Model
    ): String {
        model.addAttribute("options", SelectOptions)
        return "add-card"
    }

    @RequestMapping("")
    suspend fun createCard(
        @Valid @ModelAttribute cardFormData: CardFormData,
        errors: BindingResult,
        model: Model
    ): String {
        val cardData = CardData(cardFormData.cardNumber, cardFormData.expiryMonth + "/" + cardFormData.expiryYear)
        val cardRequestDto = CardRequestDto("PTO_1", cardData, cardFormData.accountNumber)
        val storeCardResponse = cardService.saveCardStorage(cardRequestDto)
        model.addAttribute("response", storeCardResponse)
        return "add-card"
    }
}