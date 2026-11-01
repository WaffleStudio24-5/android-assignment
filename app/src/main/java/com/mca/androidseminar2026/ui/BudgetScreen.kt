package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.uistate.BudgetUiState
import java.text.NumberFormat
import java.util.Locale

private fun formatWon(amount: Long): String =
    NumberFormat.getNumberInstance(Locale.KOREA)
        .format(amount) + "원"

@Composable
fun BudgetStatus(
    amount: Long?,
    totalPrice: Long,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "한 끼 예산",
            fontWeight = FontWeight.Bold
        )

        if (isLoading) {
            Text("예산을 불러오는 중입니다.")
        } else if (amount == null) {
            Text("아직 예산을 설정하지 않았습니다.")
        } else {
            val remaining = amount - totalPrice

            Text("설정 예산: ${formatWon(amount)}")
            Text("장바구니: ${formatWon(totalPrice)}")

            Text(
                text = if (remaining >= 0) {
                    "예산 내 · ${formatWon(remaining)} 남음"
                } else {
                    "예산 초과 · ${formatWon(-remaining)} 초과"
                },
                color = if (remaining >= 0) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    state: BudgetUiState,
    totalPrice: Long,
    onBack: () -> Unit,
    onSave: (String) -> Unit,
    onClear: () -> Unit,
    onInputChanged: () -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.amount) {
        input = state.amount?.toString() ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("한 끼 예산 관리") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← 뒤로")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "식사에 사용할 예산을 설정해 보세요.",
                style = MaterialTheme.typography.titleMedium
            )

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                BudgetStatus(
                    amount = state.amount,
                    totalPrice = totalPrice
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = input,
                    onValueChange = { next ->
                        if (
                            next.length <= 7 &&
                            next.all { it in '0'..'9' }
                        ) {
                            input = next
                            onInputChanged()
                        }
                    },
                    label = { Text("예산 금액") },
                    suffix = { Text("원") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = state.errorMessage != null,
                    supportingText = {
                        Text("1~1,000,000원")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                state.errorMessage?.let { error ->
                    Text(
                        error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = { onSave(input) },
                    enabled = !state.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (state.isSaving) "저장 중..."
                        else if (state.amount == null) "예산 설정"
                        else "예산 수정"
                    )
                }

                if (state.amount != null) {
                    OutlinedButton(
                        onClick = onClear,
                        enabled = !state.isSaving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("예산 설정 해제")
                    }
                }

                Text(
                    "예산을 초과해도 주문할 수 있습니다. " +
                            "예산은 주문 후에도 차감되지 않습니다.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
