package com.vayapay.cardIdentification.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.carddata.client.CardDataClient
import com.vayapay.cardIdentification.controller.CardIdentificationController
import com.vayapay.cardIdentification.core.CardDataService
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.CardDataDto
import com.vayapay.cardIdentification.model.CardRequestDto
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@AutoConfigureMockMvc
@SpringBootTest
class CardIdentificationControllerTest(@Value("\${bin.locationBinRangeFile}") private val binRangeLocation: String,
                                       @Value("\${card-storage.ptoId}") private val ptoid: String){

    private final var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper
    private val cardDataDto = CardDataDto("4079710420210488", "1225")
    private val cardRequestDto = CardRequestDto(cardDataDto, "123456")

    private var mockCardDataClient = mockk<CardDataClient>()
    private  var cardDataService = mockk<CardDataService>()

    private  var cardIdentificationService = mockk<CardIdentificationService>()
    private  var objectMapper = mockk<ObjectMapper>()

    init {
        cardDataService = CardDataService(mockCardDataClient)
        cardIdentificationService = CardIdentificationService(cardDataService, binRangeLocation, ptoid)
        runBlocking {
            mockMvc = MockMvcBuilders
                .standaloneSetup(CardIdentificationController(cardIdentificationService, objectMapper))
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
