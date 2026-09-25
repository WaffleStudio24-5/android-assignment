package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.uistate.CounterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
    // 상태 수정은 ViewModel 안에서만, UI에는 읽기 전용으로 공개합니다.
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        // 버튼 이벤트를 받아 count가 1 증가한 새 상태를 전달합니다.
        _uiState.update { current -> current.copy(count = current.count + 1) }
    }
}
