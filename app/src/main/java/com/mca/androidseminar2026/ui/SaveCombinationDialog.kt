package com.mca.androidseminar2026.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mca.androidseminar2026.uistate.OrderUiState

/** Dialog UI의 출발점. 입력 검증과 저장 결과 처리는 호출하는 쪽에서 완성하세요. */
@Composable
fun SaveCombinationDialog(
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

    // 팁: 이 Dialog는 실제 Dialog라고 생각되는 부분만이 아니라, 화면 전체를 덮어야 합니다.
    // 그리고 Dialog 바깥쪽 어두운 부분을 누르거나, 취소 버튼을 누르면 onDismiss를 호출해 주세요.
}
