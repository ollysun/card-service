package com.vayapay.cardIdentification.controller

import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardRequestDto
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid


@Controller
@RequestMapping("/card-registration")
class CardIdentificationController(
    val cardService: CardIdentificationService
) {

    @PostMapping
    fun cardRegistration(@RequestBody @Valid cardRegistration: CardRequestDto): String? {
        return null;
    }
}