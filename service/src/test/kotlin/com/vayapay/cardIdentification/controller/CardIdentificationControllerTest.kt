package com.vayapay.cardIdentification.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.vayapay.cardIdentification.config.WebSecurityConfig
import com.vayapay.cardIdentification.core.CardIdentificationService
import com.vayapay.cardIdentification.model.*
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext
import java.util.*

private const val ADD_CARD_URL = "/card-registration"

private const val VALID_CARD_NUMBER = "5555555555555554444"

private const val INVALID_CARD_NUMBER = ""

private const val VALID_EXPIRY_MONTH = "09"
private const val INVALID_EXPIRY_MONTH = "0"

private const val VALID_EXPIRY_YEAR = "24"
private const val INVALID_EXPIRY_YEAR = "24"

private const val CARD_NUMBER_PARAM = "cardNumber"

private const val EXPIRY_MONTH_PARAM = "expiryMonth"

private const val EXPIRY_YEAR_PARAM = "expiryYear"

private const val PTO_ID = "PTO_1"

private const val ACCOUNT_NUMBER = "1234567890"

private const val MASTER_SCHEME = "Master"


@ContextConfiguration
@WebMvcTest(CardIdentificationController::class)
@Import(WebSecurityConfig::class)
class CardIdentificationControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    private lateinit var addCardForm: AddCardForm

    @MockkBean
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var cardIdentificationService: CardIdentificationService

    private var validTestCard = LinkedMultiValueMap<String, String>()

    private var invalidTestCard = LinkedMultiValueMap<String, String>()

    @BeforeEach
    fun setup() {
        runBlocking {
            mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()

            validTestCard = createTestCard(VALID_CARD_NUMBER, VALID_EXPIRY_MONTH, VALID_EXPIRY_YEAR)
            invalidTestCard = createTestCard(INVALID_CARD_NUMBER, INVALID_EXPIRY_MONTH, INVALID_EXPIRY_YEAR)
        }
    }

    @Test
    fun `should render card registration form`() {
        runBlocking {
            mockMvc.get(ADD_CARD_URL)
                .andExpect {
                    status { isOk() }
                    view { ADD_CARD_URL }
                }
        }
    }

    @Test
    fun `should fail to add card without csrf with Forbidden error`() {
        runBlocking {
            mockMvc.post(ADD_CARD_URL) {
                params = validTestCard
            }.andExpect {
                status {
                    is4xxClientError()
                    isForbidden()
                }
                view { ADD_CARD_URL }
            }
        }
    }

    @Test
    fun `should add card`() {
        runBlocking {
            coEvery { cardIdentificationService.saveCardStorage(buildCardRequestDto()) } returns buildValidSaveCardResponse()

            mockMvc.post(ADD_CARD_URL) {
                params = validTestCard
                with(csrf())
            }.andExpect {
                status { isOk() }
                view { ADD_CARD_URL }
            }
        }
    }

    private fun createTestCard(
        cardNumber: String,
        expiryMonth: String,
        expiryYear: String
    ): LinkedMultiValueMap<String, String> {
        val testCard = LinkedMultiValueMap<String, String>()
        testCard[CARD_NUMBER_PARAM] = cardNumber
        testCard[EXPIRY_MONTH_PARAM] = expiryMonth
        testCard[EXPIRY_YEAR_PARAM] = expiryYear
        return testCard
    }

    private fun buildCardRequestDto(): CardRequestDto {
        val cardData = CardData(VALID_CARD_NUMBER, VALID_EXPIRY_MONTH + VALID_EXPIRY_YEAR)
        return CardRequestDto(PTO_ID, cardData, ACCOUNT_NUMBER)
    }

    private fun buildValidSaveCardResponse(): StoreCardResponse {
        val cardIdResponse = CardIdResponse(MASTER_SCHEME, UUID.randomUUID(), UUID.randomUUID())
        return StoreCardResponse(cardIdResponse, "", "")
    }

    private fun buildInvalidSaveCardResponse(): StoreCardResponse {
        val cardIdResponse = CardIdResponse("", UUID.randomUUID(), UUID.randomUUID())
        return StoreCardResponse(cardIdResponse, "Fail", "Invalid request")
    }


}