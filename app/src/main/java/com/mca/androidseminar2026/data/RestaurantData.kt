package com.mca.androidseminar2026.data

import androidx.annotation.DrawableRes
import com.mca.androidseminar2026.R

/** 찜 상태는 이 기준 데이터와 분리해 관리하세요. */
object RestaurantData {
    const val name = "한끼 테이블"
    const val description = "든든한 버거와 바삭한 사이드, 시원한 음료를 준비하는 작은 음식점입니다. " +
        "혼자 먹는 점심도, 함께 나누는 저녁도 한끼 테이블에서 골라 보세요."
    @DrawableRes val imageResId: Int = R.drawable.menu_burger
}
