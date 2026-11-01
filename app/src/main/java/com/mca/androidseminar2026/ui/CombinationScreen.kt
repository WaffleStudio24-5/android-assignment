package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import java.text.NumberFormat
import java.util.Locale

/** 저장된 조합 목록 화면 UI (예산 잔액 표시 및 예산 초과 카드 테두리 적용) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationScreen(
    state: CombinationUiState,
    budget: Int,
    onBack: () -> Unit,
    onShowBudgetDialog: () -> Unit,
    onLoadToCart: (Map<Int, Int>) -> Unit = {},
    onDeleteCombination: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var deletingCombination by remember { mutableStateOf<Combination?>(null) }
    val formattedBudget = NumberFormat.getNumberInstance(Locale.KOREA).format(budget)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "저장한 조합",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onShowBudgetDialog) {
                        Text(
                            text = "잔액: ${formattedBudget}원",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "자주 먹는 메뉴를 한 번에 장바구니에 담아 보세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (state.combinations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "저장한 조합이 없어요",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "장바구니에서 자주 먹는 메뉴를\n조합으로 저장해 보세요.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onBack) {
                            Text("장바구니로 돌아가기")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.combinations, key = { it.id }) { combination ->
                        CombinationCard(
                            combination = combination,
                            budget = budget,
                            onAddToCart = {
                                onLoadToCart(combination.quantities)
                                Toast.makeText(context, "'${combination.name}' 조합을 장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()
                            },
                            onDelete = {
                                deletingCombination = combination
                            }
                        )
                    }
                }
            }
        }
    }

    deletingCombination?.let { combination ->
        AlertDialog(
            onDismissRequest = { deletingCombination = null },
            title = { Text("조합을 삭제할까요?", fontWeight = FontWeight.Bold) },
            text = { Text("${combination.name}가 조합에서 삭제됩니다.\n현재 장바구니는 그대로예요.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCombination(combination.id)
                        deletingCombination = null
                    }
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { deletingCombination = null }
                ) {
                    Text("취소", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        )
    }
}

@Composable
fun CombinationCard(
    combination: Combination,
    budget: Int,
    onAddToCart: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedTotalPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(combination.totalPrice)
    val isExceeded = combination.totalPrice > budget

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isExceeded) 2.dp else 1.dp,
            color = if (isExceeded) MaterialTheme.colorScheme.error else Color(0xFFDDE0E6)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isExceeded) {
                Text(
                    text = "주의: 예산 초과",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = combination.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onDelete
                ) {
                    Text("삭제")
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                combination.itemsWithQuantity.forEach { (menu, qty) ->
                    val itemPrice = NumberFormat.getNumberInstance(Locale.KOREA).format((menu.price * qty).toLong())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${menu.name} × $qty",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${itemPrice}원",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF0EFF8))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "총 ${combination.totalQuantity}개 · ${formattedTotalPrice}원",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isExceeded) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )

                Button(onClick = onAddToCart) {
                    Text("장바구니에 담기")
                }
            }
        }
    }
}
