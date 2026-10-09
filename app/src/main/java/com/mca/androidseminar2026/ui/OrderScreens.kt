
package com.mca.androidseminar2026.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.RestaurantData
import com.mca.androidseminar2026.model.OrderTab
import com.mca.androidseminar2026.uistate.CartUiState
import kotlinx.coroutines.delay


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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    selectedCategory: MenuCategory?,
    cartUiState: CartUiState,
    onCategorySelected: (MenuCategory?) -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(500L)
            isRefreshing = false
        }
    }

    val filteredMenus = MenuData.items.filter { menu ->
        selectedCategory == null || menu.category == selectedCategory
    }

    val categories = listOf(
        null to "전체",
        MenuCategory.MAIN to "메인",
        MenuCategory.SIDE to "사이드",
        MenuCategory.DRINK to "음료"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 8.dp)
        ) {
            Text(
                text = "한끼 테이블",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "오늘 먹고 싶은 메뉴를 골라 보세요.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { (category, label) ->
                    val selected = selectedCategory == category

                    OutlinedButton(
                        onClick = { onCategorySelected(category) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selected) Color.Transparent
                            else Color(0xFFB2B4C1)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected) {
                                Color(0xFFDDE3FA)
                            } else {
                                Color.White
                            }
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    ) {
                        Text(
                            text = label,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF555966)
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                if (!isRefreshing) {
                    isRefreshing = true
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = filteredMenus,
                    key = { it.id }
                ) { menu ->
                    MenuItemRow(
                        menu = menu,
                        quantity = cartUiState.quantityOf(menu.id),
                        onIncrease = { onIncrease(menu.id) },
                        onDecrease = { onDecrease(menu.id) }
                    )
                }
            }
        }
    }
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
