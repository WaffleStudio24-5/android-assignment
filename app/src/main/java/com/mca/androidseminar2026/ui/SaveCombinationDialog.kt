package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Dialog UI의 출발점. 입력 검증과 저장 결과 처리는 호출하는 쪽에서 완성하세요. */
@Composable
fun SaveCombinationDialog(
    itemCount: Int,
    totalQuantity: Int,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }

    // TODO: 내부 구현
    // 저장 버튼이 눌리면 onSave(name)을 호출하면 됩니다.
    // onDismiss, onSave는 호출하는 쪽에서 제작해 넣어주어야 합니다.
    // onSave: name을 받아서 코루틴에서 Room DB로 로컬 저장소에 해당 name으로 된 조합 정보를 저장하는 함수
    // onDismiss: Dialog 상태를 닫힘으로 바꾸는 함수

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "조합 저장",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "메뉴 ${itemCount}종 · 총 ${totalQuantity}개를 저장합니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("조합 이름") },
                    placeholder = { Text("예: 버거 세트") },
                    singleLine = true,
                    isError = errorMessage != null,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        text = "공백을 제외하고 1~20자로 입력해 주세요.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name) }
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("취소", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    )
}
