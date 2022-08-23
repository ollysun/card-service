package com.vayapay.cardIdentification.controller

import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardFormData
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.util.SelectOptions
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@Controller
@RequestMapping("/card")
class CardController(val cardService: CardIdentificationService) {

    @RequestMapping("", method = [RequestMethod.GET])
    suspend fun addCard(model: Model): String {
        model.addAttribute("options", SelectOptions)
        val cardFormData = CardFormData()
        model.addAttribute("cardFormData", cardFormData)
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
        model.addAttribute("options", SelectOptions)

        if (storeCardResponse.errorMessage.isNotEmpty())
            model.addAttribute("saveCardError", storeCardResponse.errorMessage)
        else
            model.addAttribute("response", storeCardResponse)

        return "add-card"
    }
}