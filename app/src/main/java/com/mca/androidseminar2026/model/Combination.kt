package com.mca.androidseminar2026.model

/** 앱에서 사용하는 저장된 메뉴 조합. Room Entity는 직접 설계하세요. */
data class Combination(
    val id: Long,
    val name: String,
    val quantities: Map<Int, Int>, // 메뉴 ID -> 수량
)
