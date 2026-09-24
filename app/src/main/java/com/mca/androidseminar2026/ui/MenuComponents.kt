package com.mca.androidseminar2026.ui

import androidx.compose.runtime.Composable
import com.mca.androidseminar2026.data.MenuItem

// 메뉴·찜·장바구니 세 화면에서 같은 함수를 호출하세요. ViewModel의 구조는 직접 설계하세요.
@Composable
fun MenuItemRow(
    menu: MenuItem,
    isFavorite: Boolean,
    quantity: Int,
    onToggleFavorite: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    // TODO: 왼쪽에 이름·설명·가격, 오른쪽에 둥근 정사각형 이미지를 배치하세요.
    // 설명은 두 줄 이후 말줄임 처리하고 항목 사이에 여백과 구분선을 두세요.
    // 아래쪽에는 찜 버튼과 QuantitySelector를 나란히 배치하세요.
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    // TODO: 0이면 담기 버튼, 1 이상이면 "−  n개  +" 조작부를 표시하세요.
    // 두 상태의 영역 크기를 유지하고, 이벤트는 전달받은 콜백으로 처리하세요.
}
