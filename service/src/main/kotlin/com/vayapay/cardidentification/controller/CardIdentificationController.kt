package com.vayapay.cardidentification.controller

import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cardidentification.model.CardRequestDto
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid


@Controller
@RequestMapping("/card-registration")
class CardIdentificationController(
    val cardIdentificationService: CardIdentificationService
) {

    @PostMapping
    fun cardRegistration(@RequestBody @Valid cardRegistration: CardRequestDto): String? {
        return null;
    }
}