package com.vayapay.cardidentification.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.carddata.client.CardDataClient
import com.vayapay.cardidentification.controller.CardIdentificationController
import com.vayapay.cardidentification.core.CardDataService
import com.vayapay.cardidentification.core.CardIdentificationService
import com.vayapay.cardidentification.model.CardDataDto
import com.vayapay.cardidentification.model.CardRequestDto
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@AutoConfigureMockMvc
@SpringBootTest
class CardIdentificationControllerTest{

    private final var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper
    private val cardDataDto = CardDataDto("4079710420210488", "1225")
    private val cardRequestDto = CardRequestDto(cardDataDto, "123456")

    private var mockCardDataClient = mockk<CardDataClient>()
    private  var cardDataService = mockk<CardDataService>()

    private  var cardIdentificationService = mockk<CardIdentificationService>()

    init {
        cardDataService = CardDataService(mockCardDataClient)
        cardIdentificationService = CardIdentificationService(cardDataService)
        runBlocking {
            mockMvc = MockMvcBuilders
                .standaloneSetup(CardIdentificationController(cardIdentificationService))
                .build()
        }
    }
    @Test
    fun saveCardRegistrationWithStatus200() {
        mockMvc.post("/card-identification/cards") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(cardRequestDto)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
    }


}
