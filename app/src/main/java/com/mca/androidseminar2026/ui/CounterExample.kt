package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mca.androidseminar2026.ViewModel.CounterViewModel
import com.mca.androidseminar2026.uistate.CounterUiState

@Composable
fun CounterExample() {
    // 1. ViewModel을 가져오고 StateFlow의 현재 상태를 수집합니다.
    val counterViewModel: CounterViewModel = viewModel()
    val uiState by counterViewModel.uiState.collectAsStateWithLifecycle()

    // 2. 표시할 상태와 버튼을 눌렀을 때 실행할 함수를 UI에 전달합니다.
    CounterExampleScreen(
        uiState = uiState,
        onIncrement = { counterViewModel.increment() },
    )
}

@Composable
private fun CounterExampleScreen(uiState: CounterUiState, onIncrement: () -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.safeDrawingPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Text("상태 연결 예제", style = MaterialTheme.typography.titleLarge)
            // 3. 새 상태가 전달되면 화면의 숫자도 갱신됩니다.
            Text("누른 횟수: ${uiState.count}")
            // 4. UI는 숫자를 직접 수정하지 않고 전달받은 콜백을 호출합니다.
            Button(onClick = onIncrement) { Text("숫자 올리기") }
        }
    }
}
