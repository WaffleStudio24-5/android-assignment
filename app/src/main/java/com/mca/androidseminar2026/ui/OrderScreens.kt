package com.mca.androidseminar2026.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.RestaurantData
import com.mca.androidseminar2026.model.OrderTab
import com.mca.androidseminar2026.uistate.OrderUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/** 제공 코드: 과제 2의 화면들입니다. 조합 화면 이동은 콜백으로 연결합니다. */
@Composable
fun RestaurantScreen(isFavorite: Boolean, onToggleFavorite: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(RestaurantData.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Image(painterResource(RestaurantData.imageResId), contentDescription = RestaurantData.name, modifier = Modifier.fillMaxWidth())
        Text(RestaurantData.description)
        Button(onClick = onToggleFavorite) { Text(if (isFavorite) "찜했어요 ♥" else "가게 찜하기 ♡") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    selectedCategory: MenuCategory?,
    order: OrderUiState,
    onCategorySelected: (MenuCategory?) -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val visible = MenuData.items.filter { selectedCategory == null || it.category == selectedCategory }
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            if (!refreshing) scope.launch {
                refreshing = true
                delay(500)
                refreshing = false
            }
        },
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(RestaurantData.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("오늘 먹고 싶은 메뉴를 골라 보세요.")
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterChip(selectedCategory == null, onClick = { onCategorySelected(null) }, label = { Text("전체") })
                        MenuCategory.entries.forEach { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { onCategorySelected(category) },
                                label = { Text(category.label) },
                            )
                        }
                    }
                }
            }
            items(visible, key = { it.id }) { menu ->
                HorizontalDivider()
                MenuItemRow(
                    menu = menu,
                    quantity = order.quantityOf(menu.id),
                    onIncrease = { onIncrease(menu.id) },
                    onDecrease = { onDecrease(menu.id) },
                )
            }
        }
    }
}

@Composable
fun CartScreen(
    order: OrderUiState,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onClear: () -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToCombinations: () -> Unit,
    onShowSaveDialog: () -> Unit,
    budgetAmount: Long?,
    onNavigateToBudget: () -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            item {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("장바구니", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("담은 메뉴와 금액을 확인하세요.")
                    OutlinedButton(onClick = onClear, enabled = order.totalQuantity > 0) { Text("전체 비우기") }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = onShowSaveDialog, enabled = order.totalQuantity > 0) {
                            Text("조합 저장")
                        }
                        OutlinedButton(onClick = onNavigateToCombinations) { Text("저장한 조합") }
                        OutlinedButton(onClick = onNavigateToBudget) { Text("예산 설정") }
                    }
                    budgetAmount?.let { budget ->
                        val difference = budget - order.totalPrice
                        Text(if (difference >= 0) "남은 예산: ${NumberFormat.getNumberInstance(Locale.KOREA).format(difference)}원" else "예산 초과: ${NumberFormat.getNumberInstance(Locale.KOREA).format(-difference)}원")
                    } ?: Text("예산을 설정하면 장바구니 금액과 비교할 수 있어요.")
                }
            }
            if (order.cartItems.isEmpty()) {
                item { Text("장바구니가 비어 있어요.", Modifier.padding(20.dp)) }
            } else {
                items(order.cartItems, key = { it.id }) { menu ->
                    HorizontalDivider()
                    MenuItemRow(
                        menu = menu,
                        quantity = order.quantityOf(menu.id),
                        onIncrease = { onIncrease(menu.id) },
                        onDecrease = { onDecrease(menu.id) },
                    )
                }
            }
        }
        CheckoutButton(order.totalPrice, order.totalQuantity > 0, onCheckout)
    }
}

@Composable
fun CheckoutButton(totalPrice: Long, enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("총 금액 : ${NumberFormat.getNumberInstance(Locale.KOREA).format(totalPrice)}원")
    }
}

@Composable
fun BottomBar(selectedTab: OrderTab, totalQuantity: Int, onTabSelected: (OrderTab) -> Unit) {
    NavigationBar {
        OrderTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Text(if (tab == OrderTab.CART) totalQuantity.toString() else "•") },
                label = { Text(tab.label) },
            )
        }
    }
}
