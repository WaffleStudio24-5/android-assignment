package com.mca.androidseminar2026.ui

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import java.text.NumberFormat
import java.util.Locale

/** 저장된 조합 목록 화면 UI */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationScreen(
    state: CombinationUiState,
    onBack: () -> Unit,
    onLoadToCart: (Map<Int, Int>) -> Unit = {},
    onDeleteCombination: (Long) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var deletingCombination by remember { mutableStateOf<Combination?>(null) }

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
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.combinations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "저장된 조합이 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.combinations, key = { it.id }) { combination ->
                        CombinationCard(
                            combination = combination,
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
            title = { Text("조합 삭제", fontWeight = FontWeight.Bold) },
            text = { Text("'${combination.name}' 조합을 삭제하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCombination(combination.id)
                        deletingCombination = null
                    }
                ) {
                    Text("삭제", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingCombination = null }) {
                    Text("취소")
                }
            }
        )
    }
}

@Composable
fun CombinationCard(
    combination: Combination,
    onAddToCart: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedTotalPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(combination.totalPrice)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                combination.itemsWithQuantity.forEach { (menu, qty) ->
                    val itemPrice = NumberFormat.getNumberInstance(Locale.KOREA).format((menu.price * qty).toLong())
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${menu.name} ${qty}개",
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

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "총 ${combination.totalQuantity}개",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "총 금액 : ${formattedTotalPrice}원",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(onClick = onAddToCart) {
                    Text("장바구니에 담기")
                }
            }
        }
    }
}
