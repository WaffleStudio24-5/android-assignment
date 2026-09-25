package com.mca.androidseminar2026.ui

import androidx.compose.runtime.Composable
import com.mca.androidseminar2026.model.OrderTab

// 상태와 이벤트 연결은 CounterExample.kt의 실행 예제를 참고하세요.
// 각 화면에 필요한 상태·콜백과 사용할 ViewModel은 직접 설계하세요.
// 메뉴·장바구니 항목은 MenuComponents.kt의 MenuItemRow를 재사용하세요.
// 예시 파일 구조입니다. 한 파일에 여러 함수를 모두 저장할 필요는 없습니다.

@Composable
fun RestaurantScreen() {
    // TODO: RestaurantData의 이름·대표 이미지·소개와 가게 찜 버튼을 표시하세요.
    // 찜 여부를 구분하고, 탭 전환과 화면 회전 후에도 상태를 유지하세요.
}

@Composable
fun MenuScreen() {
    // TODO: 음식점 이름, 카테고리 선택과 MenuItemRow 목록을 배치하세요.
    // 목록 맨 위에서 당겨 새로고침하면 0.5초간 진행 표시를 보여 주세요.
    // 실제 데이터 변경 없이 기존 상태를 유지하고 중복 새로고침은 막으세요.
}

@Composable
fun CartScreen() {
    // TODO: 같은 MenuItemRow로 수량이 1 이상인 메뉴를 표시하세요.
    // 수량 변경을 연결하고, 수량이 0이면 목록에서 제거하세요.
    // 전체 비우기와 빈 목록 안내를 구현하세요.
    // 전체 수량·총가격 텍스트 대신 Bottom Bar 위에 CheckoutButton을 고정하세요.
    // 클릭 시 현재 금액의 결제 완료 Toast만 표시하고 장바구니는 유지하세요.
}

@Composable
fun CheckoutButton(totalPrice: Long, enabled: Boolean, onClick: () -> Unit) {
    // TODO: "총 금액 : nnnn원" 버튼을 구현하세요. 빈 장바구니에서는 비활성화하세요.
}

@Composable
fun BottomBar(
    selectedTab: OrderTab,
    totalQuantity: Int,
    onTabSelected: (OrderTab) -> Unit,
) {
    // TODO: 세 탭과 선택 상태, 장바구니 전체 수량을 표시하세요.
    // 탭을 누르면 onTabSelected에 해당 OrderTab을 전달하세요.
}
