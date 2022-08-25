package com.vayapay.cardidentification.repo

import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant
import java.util.*

class BinRange(
    @Id var id: Long? = 0,
    var binRangeFrom: String,
    var binRangeTo: String,
    var brand: String,
    var createTime: Instant = Instant.now()
)


interface BinRangeRepository : CoroutineCrudRepository<BinRange, Long> {
}