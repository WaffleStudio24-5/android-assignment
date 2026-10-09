package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuCategory
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.data.RestaurantData
import com.mca.androidseminar2026.model.OrderTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 상태와 이벤트 연결은 CounterExample.kt의 실행 예제를 참고하세요.
// 각 화면에 필요한 상태·콜백과 사용할 ViewModel은 직접 설계하세요.
// 메뉴·장바구니 항목은 MenuComponents.kt의 MenuItemRow를 재사용하세요.
// 예시 파일 구조입니다. 한 파일에 여러 함수를 모두 저장할 필요는 없습니다.

@Composable
fun RestaurantScreen(isFavorite: Boolean, onFavoriteClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Image(
            painter = painterResource(RestaurantData.imageResId),
            contentDescription = RestaurantData.name,
            modifier = Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(24.dp)),
            contentScale = ContentScale.Crop,
        )
        Text(RestaurantData.name, style = MaterialTheme.typography.headlineMedium)
        Text(RestaurantData.description, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onFavoriteClick) {
            Text(if (isFavorite) "♥ 찜 해제" else "♡ 가게 찜")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    selectedCategory: MenuCategory?,
    quantities: Map<Int, Int>,
    onCategorySelected: (MenuCategory?) -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val menus = MenuData.items.filter { selectedCategory == null || it.category == selectedCategory }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            if (!isRefreshing) scope.launch {
                isRefreshing = true
                delay(500)
                isRefreshing = false
            }
        },
        modifier = modifier,
    ) {
        LazyColumn(contentPadding = PaddingValues(20.dp)) {
            item { Text(RestaurantData.name, style = MaterialTheme.typography.headlineSmall) }
            item { Spacer(Modifier.height(12.dp)) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryButton("전체", selectedCategory == null) { onCategorySelected(null) }
                    MenuCategory.entries.forEach { category ->
                        CategoryButton(category.label, selectedCategory == category) { onCategorySelected(category) }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
            items(menus, key = { it.id }) { menu ->
                MenuItemRow(menu, quantities[menu.id] ?: 0, { onIncrease(menu.id) }, { onDecrease(menu.id) })
            }
        }
    }
}

@Composable
fun CartScreen(
    quantities: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val cartMenus = MenuData.items.filter { (quantities[it.id] ?: 0) > 0 }
    val totalPrice = cartMenus.sumOf { it.price.toLong() * (quantities[it.id] ?: 0) }

    Column(modifier.padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("장바구니", style = MaterialTheme.typography.headlineSmall)
            OutlinedButton(onClick = onClear, enabled = cartMenus.isNotEmpty()) { Text("전체 비우기") }
        }
        if (cartMenus.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("장바구니가 비어 있어요.")
            }
        } else {
            LazyColumn(Modifier.weight(1f)) {
                items(cartMenus, key = { it.id }) { menu ->
                    MenuItemRow(menu, quantities[menu.id] ?: 0, { onIncrease(menu.id) }, { onDecrease(menu.id) })
                }
            }
        }
        CheckoutButton(totalPrice, cartMenus.isNotEmpty()) {
            Toast.makeText(context, "${totalPrice}원 결제 완료!", Toast.LENGTH_SHORT).show()
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun CheckoutButton(totalPrice: Long, enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
        Text("총 금액 : ${totalPrice}원")
    }
}

@Composable
fun BottomBar(
    selectedTab: OrderTab,
    totalQuantity: Int,
    onTabSelected: (OrderTab) -> Unit,
) {
    NavigationBar {
        OrderTab.entries.forEach { tab ->
            val label = if (tab == OrderTab.CART && totalQuantity > 0) "${tab.label} (${totalQuantity})" else tab.label
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Text(if (tab == OrderTab.RESTAURANT) "⌂" else if (tab == OrderTab.MENU) "☰" else "🛒") },
                label = { Text(label) },
            )
        }
    }
}

@Composable
private fun CategoryButton(label: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) Button(onClick = onClick) { Text(label) }
    else OutlinedButton(onClick = onClick) { Text(label) }
}
