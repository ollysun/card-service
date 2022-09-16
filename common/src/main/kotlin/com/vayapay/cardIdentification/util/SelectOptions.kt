package com.vayapay.cardIdentification.util

import java.time.LocalDate

private const val ADD_YEAR_VALUE = 10
private const val MINUS_YEAR_VALUE = 13
private const val CENTURY_VALUE = 2000

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
        val maxYear = LocalDate.now().year.plus(ADD_YEAR_VALUE)
        val minYear = maxYear - MINUS_YEAR_VALUE

        val years = mutableListOf<String>()

        for (year in minYear..maxYear)
            years.add((year - CENTURY_VALUE).toString())

        return years
    }

    val YEARS = getYears()

}