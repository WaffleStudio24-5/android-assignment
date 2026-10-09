package com.mca.androidseminar2026.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.androidseminar2026.data.RestaurantData
import com.mca.androidseminar2026.model.OrderTab


// 상태와 이벤트 연결은 CounterExample.kt의 실행 예제를 참고하세요.
// 각 화면에 필요한 상태·콜백과 사용할 ViewModel은 직접 설계하세요.
// 메뉴·장바구니 항목은 MenuComponents.kt의 MenuItemRow를 재사용하세요.
// 예시 파일 구조입니다. 한 파일에 여러 함수를 모두 저장할 필요는 없습니다.


@Composable
fun RestaurantScreen(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "가게 소개",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(RestaurantData.imageResId),
            contentDescription = RestaurantData.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = RestaurantData.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = RestaurantData.description,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFB2B4C1)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF555966)
            )
        ) {
            Text(
                text = if (isFavorite) {
                    "❤️ 가게 찜 해제"
                } else {
                    "♡ 가게 찜"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
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
