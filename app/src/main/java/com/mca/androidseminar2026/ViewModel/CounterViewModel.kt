package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.uistate.CounterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterViewModel : ViewModel() {
    // 교재에 있는 예시 코드보다 조금 더 복잡하죠?
    // 이게 굉장히 표준적인 구조이고, 이렇게 짠 이유가 있습니다.
    // 이 역시 고민해보거나, GPT에게 물어보면 좋을 거 같아요.
    // 교재처럼 flow를 MutableStateFlow로 하나만 짜고, 그걸 UI에 그대로 공개하면
    // 생길 수 있는 문제점에 대해 생각해보면 좋은 힌트가 될 것입니다.
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        // 버튼 이벤트를 받아 count가 1 증가한 새 상태를 전달합니다.
        _uiState.update { current -> current.copy(count = current.count + 1) }
    }
}
