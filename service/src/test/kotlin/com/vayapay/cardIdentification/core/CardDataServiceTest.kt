package com.vayapay.cardIdentification.core

import com.vayapay.carddata.client.CardDataClient
import com.vayapay.carddata.domain.CardData
import com.vayapay.carddata.domain.CardId
import com.vayapay.carddata.domain.CardScheme
import com.vayapay.carddata.messages.StoreAndLinkCardDataResponse
import com.vayapay.cardIdentification.model.CardDataDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class CardDataServiceTest {

    private val mockCardDataClient = mockk<CardDataClient>()
    private val mockCardData = mockk<List<CardData>>()
    private val cardDataService = CardDataService(mockCardDataClient)
    private val cardDataDto = CardDataDto("4079710420210488", "1225")

    @Test
    fun `should store and link cards successfully`() {
        val ptoId = "flytoget"
        val cardData = listOf(
            CardData(pan = cardDataDto.pan, expirationDate = cardDataDto.expirationDate, cardSequence = 1, iccData = ByteArray(1))
        )
        val cardId = listOf(CardId(CardScheme.VISA, UUID.randomUUID(), UUID.randomUUID()))
        val storeAndLinkCardDataResponse = StoreAndLinkCardDataResponse(cardId)

        coEvery { mockCardDataClient.storeAndLinkCardData(any(), any()) } returns cardId

        runBlocking {
            val result = cardDataService.storeCardData(ptoId, cardData)
            Assertions.assertEquals(storeAndLinkCardDataResponse, result)
        }
    }

    @Test
    fun `should return null when card data service returns null`() {
        val ptoId = "flytoget"

        coEvery { mockCardDataClient.storeAndLinkCardData(any(), any()) } returns emptyList()

        runBlocking {
            val result = cardDataService.storeCardData(ptoId, mockCardData)
            Assertions.assertNull(result)
        }
    }

}