package com.mca.androidseminar2026.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

/** 예산 수정 팝업 다이얼로그 (숫자 입력, 증감 버튼, 실시간 초과 테두리 검증) */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BudgetEditDialog(
    currentBudget: Int,
    currentCartTotal: Long,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit,
) {
    val context = LocalContext.current
    var textInput by remember { mutableStateOf(currentBudget.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val parsedAmount = textInput.toIntOrNull() ?: 0
    val isLessThanCart = parsedAmount < currentCartTotal

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "예산 수정",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "사용할 한 끼 예산 잔액을 설정하세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 증감 버튼 (+10,000, +1,000, -1,000, -10,000)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val adjustments = listOf(10000, 1000, -1000, -10000)
                    adjustments.forEach { delta ->
                        OutlinedButton(
                            onClick = {
                                val current = textInput.toIntOrNull() ?: 0
                                val next = (current + delta).coerceAtLeast(0)
                                textInput = next.toString()
                                errorMessage = null
                            }
                        ) {
                            Text(if (delta > 0) "+${NumberFormat.getNumberInstance(Locale.KOREA).format(delta)}" else NumberFormat.getNumberInstance(Locale.KOREA).format(delta))
                        }
                    }
                }

                OutlinedTextField(
                    value = textInput,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            textInput = input
                            errorMessage = null
                        }
                    },
                    label = { Text("예산 잔액 (원)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errorMessage != null || isLessThanCart,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLessThanCart && errorMessage == null) {
                    Text(
                        text = "주의: 입력한 예산이 현재 장바구니 합계보다 적습니다.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = textInput.toIntOrNull()
                    if (amount == null || amount < 0) {
                        errorMessage = "올바른 숫자를 입력해주세요."
                    } else {
                        onSave(amount)
                        Toast.makeText(context, "예산이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
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
