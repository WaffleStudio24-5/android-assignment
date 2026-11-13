package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.RecommendationRepository
import com.mca.androidseminar2026.uistate.RecommendationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException

/** TODO: Room 관찰 결과와 갱신 상태를 UI에 연결하세요. */
@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            repository.observeCache().collectLatest { snapshot ->
                _uiState.update { it.copy(recommendations = snapshot?.items ?: emptyList(), updatedAt = snapshot?.updatedAtEpochMillis) }
            }
        }
        refresh()
    }

    /** TODO: 중복 요청을 막고 코루틴에서 갱신·실패·취소를 처리하세요. */
    fun refresh() {
        if (refreshJob?.isActive == true) return
        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
        refreshJob = viewModelScope.launch {
            try {
                repository.refresh()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "최신 추천을 불러오지 못했어요.") }
            } finally {
                _uiState.update { it.copy(isRefreshing = false) }
            }
        }
    }
}
