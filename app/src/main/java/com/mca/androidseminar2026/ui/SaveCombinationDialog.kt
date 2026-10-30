package com.mca.androidseminar2026.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
@Composable
fun SaveCombinationDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("메뉴 조합 저장") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("조합 이름") },
                supportingText = { Text(errorMessage ?: "앞뒤 공백을 제외하고 1~20자로 입력하세요.") },
                isError = errorMessage != null,
                singleLine = true,
            )
        },
        confirmButton = { Button(onClick = { onSave(name) }) { Text("저장") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } },
    )
}
