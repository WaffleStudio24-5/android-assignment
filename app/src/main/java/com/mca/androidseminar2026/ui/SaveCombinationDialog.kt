
package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mca.androidseminar2026.uistate.OrderUiState

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
        onDismissRequest = {
            if (!isSaving) onDismiss()
        },
        properties = DialogProperties(
            dismissOnBackPress = !isSaving,
            dismissOnClickOutside = !isSaving
        )
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "메뉴 조합 저장",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "현재 장바구니의 메뉴 ${order.totalQuantity}개를 저장합니다."
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("조합 이름") },
                    placeholder = { Text("예: 나만의 버거 세트") },
                    singleLine = true,
                    isError = errorMessage != null,
                    supportingText = {
                        Text("${name.trim().length}/20자")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        enabled = !isSaving
                    ) {
                        Text("취소")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = { onSave(name) },
                        enabled = !isSaving && order.totalQuantity > 0
                    ) {
                        Text(if (isSaving) "저장 중..." else "저장")
                    }
                }
            }
        }
    }
}


// TODO: 내부 구현
    // 저장 버튼이 눌리면 onSave(name)을 호출하면 됩니다.
    // onDismiss, onSave는 호출하는 쪽에서 제작해 넣어주어야 합니다.
    // onSave: name을 받아서 코루틴에서 Room DB로 로컬 저장소에 해당 name으로 된 조합 정보를 저장하는 함수
    // onDismiss: Dialog 상태를 닫힘으로 바꾸는 함수

    // 팁: 이 Dialog는 실제 Dialog라고 생각되는 부분만이 아니라, 화면 전체를 덮어야 합니다.
    // 그리고 Dialog 바깥쪽 어두운 부분을 누르거나, 취소 버튼을 누르면 onDismiss를 호출해 주세요.
