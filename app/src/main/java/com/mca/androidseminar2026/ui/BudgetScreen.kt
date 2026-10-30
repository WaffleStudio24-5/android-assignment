package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp

@Composable
fun BudgetScreen(currentBudget: Long?, onSave: (String) -> Unit, onBack: () -> Unit) {
    var input by remember(currentBudget) { mutableStateOf(currentBudget?.toString().orEmpty()) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("한 끼 예산 설정")
        Text("장바구니 금액과 비교해 남은 예산 또는 초과 금액을 보여 줍니다.")
        OutlinedTextField(
            value = input,
            onValueChange = { input = it; error = null },
            label = { Text("예산(원)") },
            isError = error != null,
            supportingText = { error?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = {
            if (input.trim().toLongOrNull()?.takeIf { it > 0 } == null) error = "0보다 큰 금액을 입력해 주세요."
            else { onSave(input); onBack() }
        }) { Text("저장") }
        OutlinedButton(onClick = onBack) { Text("취소") }
    }
}
