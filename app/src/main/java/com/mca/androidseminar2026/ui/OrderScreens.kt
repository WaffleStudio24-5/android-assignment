package com.mca.androidseminar2026.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
    onShowBudgetDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var refreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val visible = MenuData.items.filter { selectedCategory == null || it.category == selectedCategory }
    val formattedTotal = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
    val formattedBudget = NumberFormat.getNumberInstance(Locale.KOREA).format(order.budget)
    val available = order.availableBudget
    val formattedAvailable = NumberFormat.getNumberInstance(Locale.KOREA).format(kotlin.math.abs(available))
    val budgetStatusText = if (available >= 0) "남은 예산: ${formattedAvailable}원" else "예산 초과"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(RestaurantData.name, fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onShowBudgetDialog) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${formattedTotal}원 / ${formattedBudget}원",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = budgetStatusText,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (available < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                if (!refreshing) scope.launch {
                    refreshing = true
                    delay(500)
                    refreshing = false
                }
            },
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
}

@OptIn(ExperimentalMaterial3Api::class)
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
    onShowBudgetDialog: () -> Unit,
) {
    val formattedTotal = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
    val formattedBudget = NumberFormat.getNumberInstance(Locale.KOREA).format(order.budget)
    val available = order.availableBudget
    val formattedAvailable = NumberFormat.getNumberInstance(Locale.KOREA).format(kotlin.math.abs(available))
    val availableText = if (available >= 0) "사용 가능 예산: ${formattedAvailable}원" else "사용 가능 예산: 예산 초과"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("장바구니", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onShowBudgetDialog) {
                        Text(
                            text = "예산: ${formattedBudget}원",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(Modifier.weight(1f)) {
                item {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("담은 메뉴와 금액을 확인하세요.")

                        // 예산 상태 카드 (현재 합계 / 예산 잔액 + 수정 버튼, 사용 가능 예산)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${formattedTotal}원 / ${formattedBudget}원",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.isCartExceeded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                    )
                                    OutlinedButton(
                                        onClick = onShowBudgetDialog,
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                           Text("수정")
                                    }
                                }
                                Text(
                                    text = availableText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (available < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        OutlinedButton(onClick = onClear, enabled = order.totalQuantity > 0) { Text("전체 비우기") }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onShowSaveDialog,
                                enabled = order.totalQuantity > 0,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                                )
                            ) {
                                Text("조합 저장")
                            }
                            OutlinedButton(onClick = onNavigateToCombinations) { Text("저장한 조합") }
                        }
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
            CheckoutButton(order, onCheckout)
        }
    }
}

@Composable
fun CheckoutButton(order: OrderUiState, onCheckout: () -> Unit) {
    val formattedTotal = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (order.isCartExceeded) {
            Text(
                text = "주의: 예산 초과",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = onCheckout,
            enabled = order.totalQuantity > 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "총 금액 : ${formattedTotal}원",
                color = if (order.isCartExceeded) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onPrimary
            )
        }
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
