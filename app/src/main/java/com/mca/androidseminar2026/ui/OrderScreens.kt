package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mca.androidseminar2026.ViewModel.OrderViewModel
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.RestaurantData
import com.mca.androidseminar2026.model.OrderTab
import java.text.NumberFormat

@Composable
fun RestaurantScreen(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = "가게 소개", fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = RestaurantData.imageResId),
            contentDescription = RestaurantData.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = RestaurantData.name, fontSize = 32.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        Text(RestaurantData.description)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onFavoriteClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFB7B7C2)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF62636D)
            )
        ) {
            Text(
                if (isFavorite) "♥ 가게 찜 해제"
                else "♡ 가게 찜하기"
            )
        }
    }
}

@Composable
fun MenuScreen(
    viewModel: OrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val visibleItems = MenuData.items.filter { menu ->
        uiState.selectedCategory == null || menu.category == uiState.selectedCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = RestaurantData.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            FilterChip(
                selected = uiState.selectedCategory == null,
                onClick = { viewModel.selectCategory(null) },
                label = { Text("전체") }
            )
            FilterChip(
                selected = uiState.selectedCategory == MenuCategory.MAIN,
                onClick = { viewModel.selectCategory(MenuCategory.MAIN) },
                label = { Text("메인") }
            )
            FilterChip(
                selected = uiState.selectedCategory == MenuCategory.SIDE,
                onClick = { viewModel.selectCategory(MenuCategory.SIDE) },
                label = { Text("사이드") }
            )
            FilterChip(
                selected = uiState.selectedCategory == MenuCategory.DRINK,
                onClick = { viewModel.selectCategory(MenuCategory.DRINK) },
                label = { Text("음료") }
            )
        }

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refreshMenu,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(visibleItems) { menu ->
                    MenuItemRow(
                        menu = menu,
                        quantity = uiState.quantities[menu.id] ?: 0,
                        onIncrease = { viewModel.increaseQuantity(menu.id) },
                        onDecrease = { viewModel.decreaseQuantity(menu.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    viewModel: OrderViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val formattedPrice = NumberFormat.getInstance().format(uiState.totalPrice)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "장바구니",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = { viewModel.clearCart() },
                enabled = !uiState.isCartEmpty
            ) {
                Text("전체 비우기")
            }
        }

        Divider()

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (uiState.isCartEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "장바구니가 비어 있습니다.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.cartItems) { (menu, quantity) ->
                        MenuItemRow(
                            menu = menu,
                            quantity = quantity,
                            onIncrease = { viewModel.increaseQuantity(menu.id) },
                            onDecrease = { viewModel.decreaseQuantity(menu.id) }
                        )
                        Divider()
                    }
                }
            }
        }

        CheckoutButton(
            totalPrice = uiState.totalPrice,
            enabled = !uiState.isCartEmpty,
            onClick = {
                Toast.makeText(context, "${formattedPrice}원 결제 완료!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun CheckoutButton(
    totalPrice: Long,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val formattedPrice = NumberFormat.getInstance().format(totalPrice)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(52.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "총 금액 : ${formattedPrice}원",
            fontSize = 18.sp,
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
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == OrderTab.RESTAURANT,
            onClick = { onTabSelected(OrderTab.RESTAURANT) },
            icon = { Text("가") },
            label = { Text("가게 소개") },
        )

        NavigationBarItem(
            selected = selectedTab == OrderTab.MENU,
            onClick = { onTabSelected(OrderTab.MENU) },
            icon = { Text("메") },
            label = { Text("메뉴") },
        )

        NavigationBarItem(
            selected = selectedTab == OrderTab.CART,
            onClick = { onTabSelected(OrderTab.CART) },
            icon = { Text(totalQuantity.toString()) },
            label = { Text("장바구니") }
        )
    }
}
