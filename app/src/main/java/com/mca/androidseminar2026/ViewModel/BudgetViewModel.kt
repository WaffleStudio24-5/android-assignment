package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.local.BudgetRepository
import com.mca.androidseminar2026.uistate.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.budget
                .catch { error ->
                    if (error is CancellationException) throw error

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "예산을 불러오지 못했습니다."
                        )
                    }
                }
                .collect { amount ->
                    _uiState.update {
                        it.copy(
                            amount = amount,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun saveBudget(input: String) {
        if (_uiState.value.isSaving) return

        val amount = input.trim().toLongOrNull()

        if (amount == null || amount !in 1L..1_000_000L) {
            _uiState.update {
                it.copy(
                    errorMessage = "예산은 1~1,000,000원으로 입력해 주세요."
                )
            }
            return
        }

        _uiState.update {
            it.copy(isSaving = true, errorMessage = null)
        }

        viewModelScope.launch {
            try {
                repository.saveBudget(amount)
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.update {
                    it.copy(errorMessage = "예산 저장에 실패했습니다.")
                }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun clearBudget() {
        if (_uiState.value.isSaving) return

        _uiState.update {
            it.copy(isSaving = true, errorMessage = null)
        }

        viewModelScope.launch {
            try {
                repository.clearBudget()
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.update {
                    it.copy(errorMessage = "예산 해제에 실패했습니다.")
                }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
