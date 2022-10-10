package com.vayapay.cardidentification.core


import com.vayapay.cardidentification.model.BinRangeModel
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile


internal class BinRangeServiceTest {
    private val mockBinRangeConfiguration = mockk<BinRangeConfiguration>()
    private var binRangeService =  mockk<BinRangeService>()
    private val mockBinRangeJsonModel = mockk<List<BinRangeModel>>()
    init {
        binRangeService =  BinRangeService(mockBinRangeConfiguration)
    }
    @Test
    fun `should upload bin ranges file`() {
        val mockMultipartFile = MockMultipartFile("file", "elavontest.csv",
            "text/plain",
            "some csv".toByteArray())
           binRangeService.binRangeLocation = "service/build/data/BinRange.json"
        runBlocking {
            val result = binRangeService.uploadBinRangesFile(mockMultipartFile)
            binRangeService.processAndReturnJson(mockBinRangeJsonModel)
            Assertions.assertEquals("json created", result)
        }


    }


}