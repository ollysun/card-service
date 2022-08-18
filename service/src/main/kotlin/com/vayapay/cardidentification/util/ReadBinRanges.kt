package com.vayapay.cardidentification.util

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


@Component
class ReadBinRanges {

    fun readfromResourcefolder(){
        //val readText = arrayOf<Int>()

        try{
            val cpr = ClassPathResource("ELAVON_ACCTRNG_20220729.csv")
            val reader = BufferedReader(InputStreamReader(cpr.inputStream))
            reader.lines().forEach {
                val readText: List<String> = it.split(",")
                validateString(readText)
            }

        }catch (e :IOException){

        }
    }

    fun validateString(txt: List<String>){
        var binrangeFrom : String = ""
        var binrangeTo : String = ""
        if(txt[0].length == 12){
            binrangeFrom = txt[0]
        }

        if(txt[1].length == 12){
            binrangeTo = txt[1];
        }



        txt.forEach({
            if (it.length == 12){

            }

        })
    }
//‘LA’ = Laser Card – Debit Card
//‘MC’ = MasterCard – Debit/Credit Card
//‘MD’ = MasterCard Maestro – Debit Card
//‘SH’ = Shell – n/a
//‘SW’ = Switch – Debit Card
//‘UP’ = Union Pay
//‘VD’ = Visa Debit – Debit Card
//‘VE’ = Visa Electron – Debit/Credit Card
//‘VI’ = Visa – Credit Card
//‘UP’ = Union Pay - Credit Card
//‘UD’ = Union Pay – Debit Card
    fun getBrand( chr: String){
        val binBrandName = mapOf(
            "AX" to "American Express (Amex) - Credit Card",
            "DC" to "Discover – Debit/Credit Card",
            "DI" to "Discover – Debit/Credit Card",
            "GC" to "Gift Card – n/a",
            "JC" to "Japan Credit Bureau (JCB) – Credit Card",
            "LA" to "Laser Card - Debit Card",
            "MC" to "MasterCard - Debit/Credit Card",
            "MD" to "MasterCard Maestro – Debit Card",
            "SH" to "Shell – n/a",
            "SW" to "Switch - Debit Card",
            "UP" to "Union Pay - n/a",
            "VD" to "Visa Debit – Debit Card",
            "VE" to "Visa Electron – Debit/Credit Card",
            "VI" to "Visa Electron – Debit/Credit Card"
        )

    }

}