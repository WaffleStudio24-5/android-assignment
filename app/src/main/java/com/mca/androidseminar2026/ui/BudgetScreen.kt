package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.uistate.BudgetUiState
import java.text.NumberFormat
import java.util.Locale

/** 금액을 읽기 쉬운 원 단위로 표시합니다. */
fun formatWon(amount: Long): String = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"

/** 메뉴와 장바구니에서 같은 기준으로 예산과 현재 금액을 비교합니다. */
@Composable
fun BudgetSummary(state: BudgetUiState, totalPrice: Long, onEdit: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val amount = state.amount
            Text(when {
                state.isLoading -> "예산을 불러오는 중이에요."
                amount == null -> "한 끼 예산을 설정해 보세요."
                else -> "한 끼 예산 ${formatWon(amount)} · 현재 ${formatWon(totalPrice)}"
            })
            if (amount != null) {
                Text(
                    if (totalPrice > amount) "예산보다 ${formatWon(totalPrice - amount)} 초과했어요." else "예산까지 ${formatWon(amount - totalPrice)} 남았어요.",
                    color = if (totalPrice > amount) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )
            }
            state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            TextButton(onClick = onEdit) { Text("예산 설정") }
        }
    }
}

/** 한 끼 예산 입력·수정·해제를 제공합니다. */
@Composable
fun BudgetScreen(state: BudgetUiState, onBack: () -> Unit, onSave: (String) -> Unit, onClear: () -> Unit, onRetry: () -> Unit) {
    var input by rememberSaveable(state.amount) { mutableStateOf(state.amount?.toString().orEmpty()) }
    Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(onClick = onBack, enabled = !state.isSaving) { Text("뒤로") }
        Text("한 끼 예산", style = MaterialTheme.typography.headlineLarge)
        Text("한 번의 식사에 사용할 기준 금액이에요. 초과해도 주문할 수 있고, 주문 후에도 예산은 차감되지 않아요.")
        OutlinedTextField(
            value = input, onValueChange = { input = it }, label = { Text("예산 (원)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true, enabled = !state.isLoading && !state.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )
        state.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            TextButton(onClick = onRetry, enabled = !state.isSaving) { Text("저장된 예산 다시 읽기") }
        }
        Button(onClick = { onSave(input) }, enabled = !state.isLoading && !state.isSaving) { Text(if (state.isSaving) "저장 중…" else "예산 저장") }
        TextButton(onClick = onClear, enabled = !state.isLoading && !state.isSaving && state.amount != null) { Text("예산 해제") }
    }
}
