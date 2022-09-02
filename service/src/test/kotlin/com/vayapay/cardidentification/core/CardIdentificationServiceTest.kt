package com.vayapay.cardidentification.core

import org.junit.Test
import org.junit.jupiter.api.Assertions.*



class CardIdentificationServiceTest(private val cardIdentificationService: CardIdentificationService) {

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
}