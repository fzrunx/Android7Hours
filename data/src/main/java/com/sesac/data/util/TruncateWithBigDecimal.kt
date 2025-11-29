package com.sesac.data.util

import java.math.BigDecimal
import java.math.RoundingMode

fun truncateWithBigDecimal(value: Double, n: Int): Double {
    require(n >= 0) { "n must be >= 0" }
    return BigDecimal.valueOf(value)
        .setScale(n, RoundingMode.DOWN)
        .toDouble()
}