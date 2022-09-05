package com.vayapay.cardidentification.core


import com.vayapay.carddata.client.CardDataClient
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.domain.CardId
import com.vayapay.carddata.domain.CardScheme
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.model.CardDataDto
import com.vayapay.cardidentification.model.CardRequestDto
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import java.util.*


class CardIdentificationServiceTest{
    private val mockCardDataClient = mockk<CardDataClient>()
    private val mockCardId = mockk<CardId>()
    private val cardDataService = CardDataService(mockCardDataClient)
    lateinit var cardIdentificationService: CardIdentificationService
    private val cardDataDto = CardDataDto("4079710420210488", "1225")

    val cardRequestDto = CardRequestDto(cardDataDto)
    @BeforeEach
    fun init() {
        cardIdentificationService = CardIdentificationService(cardDataService)
    }

    @Test
    fun luhmCheck() {
        assertTrue(cardIdentificationService.luhnCheck("4957030420210488"))
        assertFalse(cardIdentificationService.luhnCheck("4242424242424244"))
    }

    @Test
    fun isDigitNumber() {
        assertTrue(cardIdentificationService.isDigitNumber("11"))
        assertFalse(cardIdentificationService.isDigitNumber("11.0F"))
    }

    @Test
    fun saveCardStorageTest() {

        val ptoId = "flytoget"

        val cardData = listOf(
            CardData(pan = cardDataDto.pan, expirationDate = cardDataDto.expirationDate, cardSequence = 1, iccData = ByteArray(1))
        )
        val cardid = listOf(CardId(CardScheme.VISA, UUID.randomUUID(), UUID.randomUUID()))
        val storeAndLinkCardDataResponse = StoreAndLinkCardDataResponse(cardid)
        runBlocking {
            every { runBlocking { mockCardDataClient.storeAndLinkCardData(any(), any()) } } returns cardid
            val result = cardDataService.storeCardData(StoreAndLinkCardDataRequest(ptoId, cardData))
            assertEquals(storeAndLinkCardDataResponse, result)
        }
    }

    @Test
    fun checkValidationPanBinRange() {
        assertTrue(cardIdentificationService.validationPanBinRange("4079710420210488"))
    }
}