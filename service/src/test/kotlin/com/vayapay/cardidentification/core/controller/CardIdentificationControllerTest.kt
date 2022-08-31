package com.vayapay.cardidentification.core.controller

import com.vayapay.cardidentification.controller.BinRangesImportController
import com.vayapay.cardidentification.controller.CardIdentificationController
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@WebMvcTest(CardIdentificationController::class)
class CardIdentificationControllerTest(
    @Autowired val cardIdentificationController: CardIdentificationController) {

    
}