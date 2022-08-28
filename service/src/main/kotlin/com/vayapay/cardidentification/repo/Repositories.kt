package com.vayapay.cardidentification.repo

import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.Instant
import java.util.*

class BinRange(
    val id: Int,
    val binRangeFrom: String,
    val binRangeTo: String,
    val brand: String,
    val createTime: Instant = Instant.now()
)


