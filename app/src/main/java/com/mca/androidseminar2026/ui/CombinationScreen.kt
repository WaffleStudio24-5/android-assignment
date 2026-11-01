
package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationScreen(
    state: CombinationUiState,
    onBack: () -> Unit,
    onAdd: (Map<Int, Int>) -> Unit,
    onDelete: (Long) -> Unit,
) {
    var pendingDelete by remember {
        mutableStateOf<Combination?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("저장한 조합") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← 뒤로")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.errorMessage?.let { error ->
                    item {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                if (state.combinations.isEmpty()) {
                    item {
                        Text(
                            text = "아직 저장한 조합이 없습니다.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(
                        items = state.combinations,
                        key = { it.id }
                    ) { combination ->
                        CombinationCard(
                            combination = combination,
                            onAdd = {
                                onAdd(combination.quantities)
                            },
                            onDelete = {
                                pendingDelete = combination
                            }
                        )
                    }
                }
            }
        }
    }

    pendingDelete?.let { combination ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("조합 삭제") },
            text = {
                Text("'${combination.name}' 조합을 삭제할까요?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(combination.id)
                        pendingDelete = null
                    }
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { pendingDelete = null }
                ) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
private fun CombinationCard(
    combination: Combination,
    onAdd: () -> Unit,
    onDelete: () -> Unit,
) {
    val totalQuantity = combination.quantities.values.sum()

    val totalPrice = combination.quantities.entries.sumOf {
            (menuId, quantity) ->
        val price = MenuData.items
            .firstOrNull { it.id == menuId }
            ?.price?.toLong() ?: 0L
        price * quantity
    }

    val formattedPrice =
        NumberFormat.getNumberInstance(Locale.KOREA)
            .format(totalPrice)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = combination.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            combination.quantities.forEach { (menuId, quantity) ->
                val menu = MenuData.items
                    .firstOrNull { it.id == menuId }

                Text(
                    text = "${menu?.name ?: "알 수 없는 메뉴"} × ${quantity}"
                )
            }

            HorizontalDivider()

            Text(
                text = "전체 ${totalQuantity}개 · ${formattedPrice}원",
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onAdd) {
                    Text("장바구니에 담기")
                }

                OutlinedButton(onClick = onDelete) {
                    Text("삭제")
                }
            }
        }
    }
}
