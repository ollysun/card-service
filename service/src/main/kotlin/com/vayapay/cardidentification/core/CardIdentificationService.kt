package com.vayapay.cardidentification.core

import com.vayapay.cardIdentification.CardDataStorageClient
import com.vayapay.cardidentification.exception.CardIdentificationException
import com.vayapay.cardidentification.model.CardIdResponse
import com.vayapay.cardidentification.model.CardRequestDto
import org.springframework.stereotype.Service

@Service
class CardIdentificationService constructor(
    val cardStorageClient: CardDataStorageClient
) {


    suspend fun saveCardStorage(cardDataRequest: CardRequestDto) {

            val bic = cardDataRequest.cardData.pan.take(6);
            if(bic != "957852"){
                throw CardIdentificationException("wrong bank identity code ")
            }

            val derivedpan = StringBuilder(bic)
            derivedpan.append(cardDataRequest.bankAccountNumber);

           val visapan : String = derivedpan.toString()

            val storeCardResponse : CardIdResponse? = cardStorageClient.storeCardData(cardDataRequest.id,cardDataRequest.cardData)
    }
}