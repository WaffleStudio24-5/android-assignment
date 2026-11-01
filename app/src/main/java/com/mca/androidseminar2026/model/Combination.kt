package com.mca.androidseminar2026.model

data class Combination(
    val id: Long,
    val name: String,
    val quantities: Map<Int, Int>, // 메뉴 ID -> 수량
)
