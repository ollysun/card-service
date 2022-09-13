package com.vayapay.cardidentification.core


import com.vayapay.carddata.client.CardDataClient
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.domain.CardId
import com.vayapay.carddata.domain.CardScheme
import com.vayapay.carddata.messages.StoreAndLinkCardDataRequest
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardidentification.model.CardDataDto
import com.vayapay.cardidentification.model.CardRequestDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import java.util.*


class CardIdentificationServiceTest{
    private val mockCardDataClient = mockk<CardDataClient>()
    private val cardDataService = CardDataService(mockCardDataClient)
    private var cardIdentificationService =  CardIdentificationService(cardDataService)
    private val cardDataDto = CardDataDto("4079710420210488", "1225")
    private val cardRequestDto = CardRequestDto(cardDataDto, "23456789")

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
        val cardId = listOf(CardId(CardScheme.VISA, UUID.randomUUID(), UUID.randomUUID()))
        val storeAndLinkCardDataResponse = StoreAndLinkCardDataResponse(cardId)

        coEvery { mockCardDataClient.storeAndLinkCardData(any(), any()) } returns cardId

        runBlocking {
            val result = cardDataService.storeCardData(ptoId, cardData)
            assertEquals(storeAndLinkCardDataResponse, result)
        }
    }

    @Test
    fun checkValidationPanBinRange() {
        assertTrue(cardIdentificationService.validationPanBinRange("4079710420210488"))
        assertFalse(cardIdentificationService.validationPanBinRange("4079870420210488"))
    }
}