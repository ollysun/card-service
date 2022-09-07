package com.vayapay.cardidentification.core.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.vayapay.cardidentification.model.CardDataDto
import com.vayapay.cardidentification.model.CardRequestDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post


@SpringBootTest
@AutoConfigureMockMvc
class CardIdentificationControllerTest(@Autowired val mockMvc: MockMvc) {

    @Autowired
    lateinit var mapper: ObjectMapper
    private val cardDataDto = CardDataDto("4079710420210488", "1225")
    private val cardRequestDto = CardRequestDto(cardDataDto, "123456")

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

