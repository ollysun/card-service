package com.vayapay.cardidentification.controller

import com.vayapay.cardidentification.core.BinRangeService
import org.springframework.http.ResponseEntity
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
    fun uploadBinRanges(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        return ResponseEntity.ok(binRangeService.uploadBinRangesFile(file))
    }
}