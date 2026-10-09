
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
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size


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
fun CartScreen(
    cartUiState: CartUiState,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onClearCart: () -> Unit,
) {
    val context = LocalContext.current

    val cartItems = MenuData.items.filter { menu ->
        cartUiState.quantityOf(menu.id) > 0
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "장바구니",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "담은 메뉴와 금액을 확인하세요.",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onClearCart,
                enabled = cartItems.isNotEmpty(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFB2B4C1)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF555966)
                )
            ) {
                Text("전체 비우기")
            }
        }

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "장바구니가 비어 있습니다.",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(
                    items = cartItems,
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAF9FD))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            CheckoutButton(
                totalPrice = cartUiState.totalPrice,
                enabled = cartItems.isNotEmpty(),
                onClick = {
                    Toast.makeText(
                        context,
                        "%,d원 결제 완료!".format(cartUiState.totalPrice),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}



@Composable
fun CheckoutButton(
    totalPrice: Long,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF50618E),
            disabledContainerColor = Color(0xFFB8BED2)
        )
    ) {
        Text(
            text = "총 금액 : %,d원".format(totalPrice),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Composable
fun BottomBar(
    selectedTab: OrderTab,
    totalQuantity: Int,
    onTabSelected: (OrderTab) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEEFF7))
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OrderTab.entries.forEach { tab ->
                val selected = selectedTab == tab

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 40.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                if (selected) Color(0xFFDDE3FA)
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (tab) {
                                OrderTab.RESTAURANT -> "⌂"
                                OrderTab.MENU -> "≡"
                                OrderTab.CART -> totalQuantity.toString()
                            },
                            fontSize = 22.sp,
                            color = Color(0xFF555966)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = tab.label,
                        fontSize = 13.sp,
                        fontWeight = if (selected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = Color(0xFF555966)
                    )
                }
            }
        }
    }
}

