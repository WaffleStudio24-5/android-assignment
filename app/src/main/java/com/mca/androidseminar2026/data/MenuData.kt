package com.mca.androidseminar2026.data

import androidx.annotation.DrawableRes
import com.mca.androidseminar2026.R

enum class MenuCategory(val label: String) {
    MAIN("메인"), SIDE("사이드"), DRINK("음료"),
}

/** 가격은 원 단위 정수입니다. 수량을 이 기준 데이터에 추가해 변경하지 마세요. */
data class MenuItem(
    val id: Int,
    val name: String,
    val category: MenuCategory,
    val price: Int,
    @param:DrawableRes val imageResId: Int,
    val description: String,
)

object MenuData {
    val items: List<MenuItem> = listOf(
        MenuItem(1, "클래식 버거", MenuCategory.MAIN, 6500, R.drawable.menu_burger,
            "도톰한 패티에 치즈와 아삭한 채소를 더했어요. 부드러운 번과 특제 소스가 어우러지는 든든한 한 끼입니다."),
        MenuItem(2, "머스터드 핫도그", MenuCategory.MAIN, 4800, R.drawable.menu_hotdog,
            "따뜻한 빵 사이에 육즙 가득한 소시지, 새콤한 머스터드로 완성한 핫도그예요."),
        MenuItem(3, "바삭 감자튀김", MenuCategory.SIDE, 2300, R.drawable.menu_fries,
            "겉은 바삭하고 속은 포슬포슬한 감자튀김. 버거와 함께 곁들이기 좋아요."),
        MenuItem(4, "그린 샐러드", MenuCategory.SIDE, 4200, R.drawable.menu_salad,
            "싱싱한 잎채소와 토마토를 담았어요. 산뜻한 드레싱을 곁들여 가볍게 즐겨 보세요."),
        MenuItem(5, "레몬에이드", MenuCategory.DRINK, 1800, R.drawable.menu_lemonade,
            "상큼한 레몬에 톡 쏘는 탄산을 더한 시원한 에이드예요."),
        MenuItem(6, "콜라", MenuCategory.DRINK, 2500, R.drawable.menu_cola,
            "얼음과 함께 시원하게 즐기는 콜라. 달콤하고 청량한 맛으로 식사를 마무리하세요."),
    )
}
