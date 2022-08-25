package com.vayapay.cardIdentification.util

import java.time.LocalDate

object SelectOptions {

    val MONTHS = listOf(
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12"
    )

    private fun getYears(): List<String> {
        val maxYear = LocalDate.now().year.plus(10)
        val minYear = maxYear - 13

        val years = mutableListOf<String>()

        for (year in minYear..maxYear)
            years.add((year - 2000).toString())

        return years
    }

    val YEARS = getYears()

}