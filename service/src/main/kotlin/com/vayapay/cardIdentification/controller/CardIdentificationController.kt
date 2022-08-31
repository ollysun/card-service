package com.vayapay.cardIdentification.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.AddCardForm
import com.vayapay.cardIdentification.model.CardData
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.util.SelectOptions
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid


private const val ADD_CARD_TEMPLATE = "add-card"

@Controller
@RequestMapping("/card-registration")
class CardIdentificationController(
    val cardService: CardIdentificationService, val objectMapper: ObjectMapper
) {
    @GetMapping
    suspend fun cardForm(model: Model): String {
        model["options"] = SelectOptions
        model["addCardForm"] = AddCardForm("", "", "")
        return ADD_CARD_TEMPLATE
    }

    @PostMapping
    suspend fun createCard(
        @Valid @ModelAttribute("addCardForm") addCardForm: AddCardForm,
        errors: BindingResult,
        model: Model
    ): String {
        model["options"] = SelectOptions
        if (errors.hasErrors()) {
            return ADD_CARD_TEMPLATE
        }
        val cardData = CardData(addCardForm.cardNumber, addCardForm.expiryMonth + "/" + addCardForm.expiryYear)
        val cardRequestDto = CardRequestDto("PTO_1", cardData, addCardForm.accountNumber)
        val storeCardResponse = cardService.saveCardStorage(cardRequestDto)

        if (storeCardResponse.errorMessage.isNotEmpty())
            model["addCardError"] = storeCardResponse.errorMessage
        else
            model["response"] = objectMapper.writeValueAsString(storeCardResponse.cardId)

        return ADD_CARD_TEMPLATE
    }
}