package com.vayapay.cardidentification.core

import com.vayapay.cardIdentify.CardDataStorageClient
import org.springframework.stereotype.Service

@Service
class CardIdentificationService constructor(
    val cardStorageClient: CardDataStorageClient
) {


    fun saveCardStorage() {

    }
}