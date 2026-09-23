package com.mca.androidseminar2026.model

/** 감상 기록 저장 요청의 처리 결과입니다. */
sealed interface SaveReviewResult {
    data object Success : SaveReviewResult

    data object Failure : SaveReviewResult
}
