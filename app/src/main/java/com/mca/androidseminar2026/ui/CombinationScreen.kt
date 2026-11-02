package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.data.MenuData
import com.mca.androidseminar2026.uistate.CombinationUiState
import com.mca.androidseminar2026.uistate.OrderUiState

/** 저장한 조합의 구성·금액·담기와 삭제 확인을 표시합니다. */
@Composable
fun CombinationScreen(
    state: CombinationUiState,
    onBack: () -> Unit,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onDelete: (Long) -> Unit,
    onRetry: () -> Unit,
) {
    var pendingDeleteId by rememberSaveable { mutableStateOf<Long?>(null) }
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            TextButton(onClick = onBack) { Text("뒤로") }
            Text("저장한 조합", style = MaterialTheme.typography.headlineLarge)
        }
        state.errorMessage?.let { message ->
            item {
                Text(message, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = onRetry) { Text("다시 읽기") }
            }
        }
        if (state.isLoading) item { CircularProgressIndicator() }
        else if (state.combinations.isEmpty()) item { Text("저장한 조합이 없어요. 장바구니에서 조합을 저장해 보세요.") }
        items(state.combinations, key = { it.id }) { combination ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(combination.name, style = MaterialTheme.typography.titleLarge)
                    val order = OrderUiState(quantities = combination.quantities)
                    MenuData.items.filter { order.quantityOf(it.id) > 0 }.forEach { menu ->
                        Text("${menu.name} ${order.quantityOf(menu.id)}개")
                    }
                    Text("전체 ${order.totalQuantity}개 · ${formatWon(order.totalPrice)}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onAddToCart(combination.quantities) }) { Text("장바구니에 담기") }
                        TextButton(onClick = { pendingDeleteId = combination.id }, enabled = state.deletingId == null) { Text("삭제") }
                    }
                }
            }
        }
    }
    val pending = state.combinations.firstOrNull { it.id == pendingDeleteId }
    if (pending != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text("조합을 삭제할까요?") },
            text = { Text("‘${pending.name}’을 삭제해도 현재 장바구니는 바뀌지 않아요.") },
            confirmButton = { TextButton(onClick = { onDelete(pending.id); pendingDeleteId = null }) { Text("삭제") } },
            dismissButton = { TextButton(onClick = { pendingDeleteId = null }) { Text("취소") } },
        )
    }
}
