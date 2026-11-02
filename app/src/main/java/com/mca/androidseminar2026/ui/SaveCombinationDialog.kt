package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mca.androidseminar2026.uistate.OrderUiState

/** 현재 장바구니의 조합 이름을 입력하고 저장 중에는 중복 조작을 막습니다. */
@Composable
fun SaveCombinationDialog(
    order: OrderUiState,
    errorMessage: String?,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    Dialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        properties = DialogProperties(dismissOnBackPress = !isSaving, dismissOnClickOutside = !isSaving, usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.padding(24.dp).widthIn(max = 440.dp).fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("조합 저장", style = MaterialTheme.typography.headlineSmall)
                Text("현재 ${order.totalQuantity}개 · ${formatWon(order.totalPrice)}")
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, label = { Text("조합 이름") },
                    supportingText = { Text(errorMessage ?: "앞뒤 공백을 제외하고 1~20자") },
                    isError = errorMessage != null, singleLine = true, enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss, enabled = !isSaving) { Text("취소") }
                    Button(onClick = { onSave(name) }, enabled = !isSaving) { Text(if (isSaving) "저장 중…" else "저장") }
                }
            }
        }
    }
}
