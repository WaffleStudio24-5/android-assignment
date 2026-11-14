package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.RecommendationRepository
import com.mca.androidseminar2026.uistate.RecommendationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState = _uiState.asStateFlow()

    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            repository.observeCache()
                .catch { error ->
                    if (error is CancellationException) throw error

                    _uiState.update {
                        it.copy(
                            isCacheLoaded = true,
                            errorMessage = "저장된 추천 목록을 읽지 못했어요."
                        )
                    }
                }
                .collect { snapshot ->
                    _uiState.update {
                        it.copy(
                            recommendations = snapshot?.items.orEmpty(),
                            hasSuccessfulCache = snapshot != null,
                            lastUpdatedAtEpochMillis =
                                snapshot?.updatedAtEpochMillis,
                            isCacheLoaded = true
                        )
                    }
                }
        }
        refresh()
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isRefreshing = true,
                    errorMessage = null
                )
            }

            try {
                repository.refresh()

                _uiState.update {
                    it.copy(errorMessage = null)
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage =
                            "최신 추천을 가져오지 못했어요. 다시 시도해 주세요."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(isRefreshing = false)
                }
            }
        }
    }
}
