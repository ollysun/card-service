package com.vayapay.cardIdentification.controller

import com.vayapay.cardIdentification.core.BinRangeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/bin-range")
class BinRangesImportController(
        val binRangeService: BinRangeService) {

    @PostMapping("/bin")
    fun uploadBinRanges(@RequestParam("file") file: MultipartFile): String {
        return binRangeService.uploadBinRangesFile(file)
    }
}