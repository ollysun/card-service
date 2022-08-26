package com.vayapay.cardidentification.controller

import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cardidentification.core.CsvService
import com.vayapay.cardidentification.model.CardRequestDto
import com.vayapay.cardidentification.repo.BinRange
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid


@Controller
@RequestMapping("/card-registration")
class CardIdentificationController(
        val csvService: CsvService) {

    @PostMapping
    fun cardRegistration(@RequestBody @Valid cardRegistration: CardRequestDto): String? {
        return null;
    }
}