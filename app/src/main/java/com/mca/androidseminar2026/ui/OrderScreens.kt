package com.mca.androidseminar2026.ui

import androidx.compose.runtime.Composable
import com.mca.androidseminar2026.model.OrderTab

// 각 화면에 필요한 상태와 이벤트 콜백을 파라미터로 추가하세요.
// 세 화면의 메뉴 항목은 MenuComponents.kt의 MenuItemRow를 재사용하세요.

@Composable
fun MenuScreen() {
    // TODO: 음식점 이름, 카테고리 선택과 MenuItemRow 목록을 배치하세요.
    // 목록 맨 위에서 당겨 새로고침하면 0.5초간 진행 표시를 보여 주세요.
    // 실제 데이터 변경 없이 기존 상태를 유지하고 중복 새로고침은 막으세요.
}

@Composable
fun FavoritesScreen() {
    // TODO: 메뉴 화면과 같은 MenuItemRow를 사용해 찜 목록을 표시하세요.
    // 찜 해제·담기·수량 변경을 연결하고, 비어 있으면 안내를 표시하세요.
}

@Composable
fun CartScreen() {
    // TODO: 같은 MenuItemRow로 수량이 1 이상인 메뉴를 표시하세요.
    // 찜·수량 변경을 연결하고, 수량이 0이면 목록에서 제거하세요.
    // 전체 비우기와 빈 목록 안내를 구현하세요. 상품별 합계 표시는 필요 없습니다.
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
