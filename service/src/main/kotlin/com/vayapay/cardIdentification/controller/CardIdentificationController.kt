package com.vayapay.cardIdentification.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.AddCardForm
import com.vayapay.cardIdentification.model.CardDataDto
import com.vayapay.cardIdentification.model.CardRequestDto
import com.vayapay.cardIdentification.util.SelectOptions
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
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
        val cardData = CardDataDto(addCardForm.cardNumber, addCardForm.expiryMonth + addCardForm.expiryYear)
        val cardRequestDto = CardRequestDto( cardData, addCardForm.accountNumber)
        val storeCardResponse = cardService.saveCardStorage(cardRequestDto)

        if (storeCardResponse.errorMessage?.isEmpty() == false)
            model["addCardError"] = storeCardResponse.errorMessage!!
        else
            model["response"] = objectMapper.writeValueAsString(storeCardResponse.cardIds)

        return ADD_CARD_TEMPLATE
    }


    @PostMapping("/cards")
    suspend fun cardRegistration(@RequestBody @Valid cardRequestDto: CardRequestDto): StoreAndLinkCardDataResponse {
        return cardService.saveCardStorage(cardRequestDto)
    }
}