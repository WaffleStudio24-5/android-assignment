package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.uistate.RecommendationUiState
import com.mca.androidseminar2026.data.MenuData
import java.text.NumberFormat
import java.util.Locale

/** TODO: 목록·로딩·캐시·빈 상태·오류와 재시도를 표시하세요. */
@Composable
fun RecommendationScreen(
    state: RecommendationUiState,
    onRefresh: () -> Unit,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(onClick = onBack) { Text("뒤로") }
        Text("추천 조합", style = MaterialTheme.typography.headlineLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onRefresh, enabled = !state.isRefreshing) { Text("새로고침") }
            if (state.isRefreshing) CircularProgressIndicator()
        }
        state.updatedAt?.let { Text("마지막 갱신: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(it))}") }
        state.errorMessage?.let { Text(if (state.recommendations.isEmpty()) "$it 다시 시도해 주세요." else "$it 저장된 추천을 계속 보여드려요.") }
        when {
            state.recommendations.isNotEmpty() -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.recommendations, key = { it.id }) { recommendation ->
                    val menus = MenuData.items.filter { recommendation.quantities.containsKey(it.id) }
                    val total = menus.sumOf { it.price.toLong() * (recommendation.quantities[it.id] ?: 0) }
                    Column(Modifier.fillMaxWidth()) {
                        Text(recommendation.name, style = MaterialTheme.typography.titleLarge)
                        Text(recommendation.description)
                        Text(menus.joinToString(", ") { "${it.name} ${recommendation.quantities[it.id]}개" })
                        Text("총 ${recommendation.quantities.values.sum()}개 · ${NumberFormat.getNumberInstance(Locale.KOREA).format(total)}원")
                        Button(onClick = { onAddToCart(recommendation.quantities) }) { Text("장바구니에 담기") }
                    }
                }
            }
            state.updatedAt != null -> Text("현재 추천 조합이 없어요.")
            !state.isRefreshing -> Text("추천 조합을 불러오지 못했어요.")
        }
    }
}
