package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CombinationScreen(
    state: CombinationUiState,
    onBack: () -> Unit,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onRequestDelete: (Combination) -> Unit,
    onCancelDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("저장한 조합", style = MaterialTheme.typography.headlineLarge)
            OutlinedButton(onClick = onBack) { Text("뒤로") }
        }
        if (state.combinations.isEmpty()) {
            Text("저장한 조합이 없어요.", Modifier.padding(top = 32.dp))
        } else {
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.combinations, key = { it.id }) { combination ->
                    CombinationRow(combination, onAddToCart, onRequestDelete)
                }
            }
        }
    }
    state.pendingDelete?.let { combination ->
        AlertDialog(
            onDismissRequest = onCancelDelete,
            title = { Text("조합 삭제") },
            text = { Text("${combination.name} 조합을 삭제할까요?") },
            confirmButton = { Button(onClick = onConfirmDelete) { Text("삭제") } },
            dismissButton = { OutlinedButton(onClick = onCancelDelete) { Text("취소") } },
        )
    }
}

@Composable
private fun CombinationRow(
    combination: Combination,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onRequestDelete: (Combination) -> Unit,
) {
    val menus = MenuData.items.filter { combination.quantities[it.id] != null }
    val details = menus.joinToString(", ") { "${it.name} ${combination.quantities[it.id]}개" }
    val totalQuantity = combination.quantities.values.sum()
    val totalPrice = menus.sumOf { it.price.toLong() * (combination.quantities[it.id] ?: 0) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(combination.name, style = MaterialTheme.typography.titleLarge)
        Text(details)
        Text("총 ${totalQuantity}개 · ${NumberFormat.getNumberInstance(Locale.KOREA).format(totalPrice)}원")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onAddToCart(combination.quantities) }) { Text("장바구니에 담기") }
            OutlinedButton(onClick = { onRequestDelete(combination) }) { Text("삭제") }
        }
    }
}
