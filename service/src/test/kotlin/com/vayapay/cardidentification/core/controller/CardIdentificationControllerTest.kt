package com.vayapay.cardidentification.core.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vayapay.carddata.domain.CardId
import com.vayapay.carddata.domain.CardScheme
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.controller.CardIdentificationController
import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cardidentification.model.CardRequestDto
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import io.mockk.every
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.util.*
import org.springframework.http.MediaType

@RunWith(SpringRunner::class)
@WebMvcTest(CardIdentificationController::class)
class CardIdentificationControllerTest(
    @Autowired val mockMvc: MockMvc) {

    @MockkBean
    lateinit var cardIdentificationService:  CardIdentificationService
    val mapper = jacksonObjectMapper()

    val cardId = listOf(CardId(CardScheme.VISA, UUID.randomUUID(), UUID.randomUUID()))
    val storeAndLinkCardDataResponse = StoreAndLinkCardDataResponse(cardId)

    @Test
    fun givenExistingBankAccount_whenGetRequest_thenReturnsBankAccountJsonWithStatus200() {
        runBlocking {
            every { runBlocking { cardIdentificationService.saveCardStorage(CardRequestDto(
                any(),
               any()
            )) } } returns storeAndLinkCardDataResponse
        }
        mockMvc.perform(post("/api/bankAccount?id=1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.bankCode").value("ING"));
    }


}

