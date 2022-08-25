package com.vayapay.cardidentification.dto

import java.time.Instant

class BinRangeDTO(
    var binRangeFrom: String,
    var binRangeTo: String,
    var brand: String,
    var createTime: Instant = Instant.now()
)