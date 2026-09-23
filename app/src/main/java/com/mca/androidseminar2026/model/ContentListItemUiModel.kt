package com.mca.androidseminar2026.model

data class ContentListItemUiModel(
    val id: Int,
    val typeLabel: String,
    val title: String,
    /** 책은 작가, 영화는 감독 이름입니다. */
    val creator: String,
    val year: String,
    val pageCount: Int?,
    val runningTimeMinutes: Int?,
    val reviewSummary: String = "",
)

// 책이라면 pageCount만, 영화라면 runningTimeMinutes만 null이 아니어야 합니다.
// 하지만 이 조건은 위 코드에 표현되어 있지 않고, 우리만 알고 있는 암묵적인 사실입니다. 따라서 위 코드는 별로 좋지 못한 코드입니다.
// 책과 영화의 서로 다른 정보를 더 안전하게 표현하려면 어떻게 개선할 수 있을까요?
// 수정해보셔도 좋습니다.