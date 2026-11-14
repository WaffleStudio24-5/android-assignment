package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.model.RecommendedCombination
import com.mca.androidseminar2026.uistate.RecommendationUiState
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecommendationScreen(
    state: RecommendationUiState,
    onRefresh: () -> Unit,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("뒤로")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "추천 조합",
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = onRefresh,
                enabled = !state.isRefreshing
            ) {
                Text("새로고침")
            }
        }

        // 갱신 중에도 기존 목록은 그대로 표시
        if (state.isRefreshing) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "최신 추천을 가져오는 중이에요.",
                style = MaterialTheme.typography.bodySmall
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!state.isCacheLoaded) {
                item {
                    Text("저장된 추천 목록을 확인하는 중이에요.")
                }
            } else if (!state.hasSuccessfulCache) {
                // 아직 성공적으로 받은 추천이 없는 상태
                item {
                    if (state.errorMessage != null) {
                        RecommendationError(
                            message = "추천 목록을 불러오지 못했어요.",
                            onRetry = onRefresh,
                            isRefreshing = state.isRefreshing
                        )
                    } else if (!state.isRefreshing) {
                        Text("아직 추천 조합을 불러오지 않았어요.")
                    }
                }
            } else {
                // 성공한 캐시가 존재하는 상태
                item {
                    val updatedAt = state.lastUpdatedAtEpochMillis

                    if (updatedAt != null) {
                        Text(
                            text = "마지막 업데이트: ${
                                formatUpdatedAt(updatedAt)
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 기존 캐시를 유지하면서 갱신 실패 안내
                if (state.errorMessage != null) {
                    item {
                        RecommendationError(
                            message = "최신 정보를 가져오지 못했어요. 저장된 추천을 계속 사용할 수 있어요.",
                            onRetry = onRefresh,
                            isRefreshing = state.isRefreshing
                        )
                    }
                }

                if (state.recommendations.isEmpty()) {
                    item {
                        Text(
                            text = "현재 추천 조합이 없어요.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(
                        items = state.recommendations,
                        key = { it.id }
                    ) { recommendation ->
                        RecommendationCard(
                            recommendation = recommendation,
                            onAddToCart = {
                                onAddToCart(recommendation.quantities)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    recommendation: RecommendedCombination,
    onAddToCart: () -> Unit,
) {
    val menuById = MenuData.items.associateBy { it.id }

    val totalQuantity = recommendation.quantities.values.sum()

    val totalPrice = recommendation.quantities.entries.sumOf {
            (menuId, quantity) ->
        (menuById[menuId]?.price?.toLong() ?: 0L) * quantity
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = recommendation.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = recommendation.description,
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider()

            recommendation.quantities.forEach { (menuId, quantity) ->
                val menu = menuById[menuId]

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${menu?.name ?: "알 수 없는 메뉴"} × $quantity",
                        modifier = Modifier.weight(1f)
                    )

                    if (menu != null) {
                        Text(
                            text = formatPrice(
                                menu.price.toLong() * quantity
                            )
                        )
                    }
                }
            }

            HorizontalDivider()

            Text(
                text = "전체 수량: ${totalQuantity}개",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "총금액: ${formatPrice(totalPrice)}",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = onAddToCart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("장바구니에 담기")
            }
        }
    }
}

@Composable
private fun RecommendationError(
    message: String,
    onRetry: () -> Unit,
    isRefreshing: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Button(
                onClick = onRetry,
                enabled = !isRefreshing
            ) {
                Text("다시 시도")
            }
        }
    }
}

private fun formatPrice(price: Long): String {
    return NumberFormat.getNumberInstance(Locale.KOREA)
        .format(price) + "원"
}

private fun formatUpdatedAt(timestamp: Long): String {
    return SimpleDateFormat(
        "yyyy.MM.dd HH:mm",
        Locale.KOREA
    ).format(Date(timestamp))
}
