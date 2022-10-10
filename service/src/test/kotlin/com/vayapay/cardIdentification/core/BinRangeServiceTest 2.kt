package com.vayapay.cardIdentification.core


import com.vayapay.cardIdentification.model.BinRangeModel
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile

@SpringBootTest
class BinRangeServiceTest(@Value("\${bin.locationBinRangeFile}") private val binRangeLocation: String,
                          @Value("\${bin.elavon-file}") private val elavonLocation: String) {
    private val mockBinRangeConfiguration = mockk<BinRangeConfiguration>()
    private val binRangeService = BinRangeService(mockBinRangeConfiguration, binRangeLocation, elavonLocation)
    private val mockBinRangeJsonModel = mockk<List<BinRangeModel>>()
    
    @Test
    fun `should upload bin ranges file`() {
        val mockMultipartFile = MockMultipartFile("file", "elavontest.csv",
            "text/plain",
            "some csv".toByteArray())

        runBlocking {
            val result = binRangeService.uploadBinRangesFile(mockMultipartFile)
            binRangeService.processAndReturnJson(mockBinRangeJsonModel)
            Assertions.assertEquals("json created", result)
        }


    }


}