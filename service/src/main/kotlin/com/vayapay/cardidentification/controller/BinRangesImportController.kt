package com.vayapay.cardidentification.controller

import com.vayapay.cardidentification.core.BinRangeService
import com.vayapay.cardidentification.model.BinRange
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile


@Controller
@RequestMapping("/bin-range")
class BinRangesImportController(
        val binRangeService: BinRangeService) {

    @PostMapping
    suspend fun uploadBinRanges(@RequestParam("file") file: MultipartFile): ResponseEntity<List<BinRange>> {
        binRangeService.uploadBinRangesFile(file)

        return ResponseEntity.ok(binRangeService.uploadBinRangesFile(file))
    }
}