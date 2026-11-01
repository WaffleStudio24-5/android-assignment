package com.mca.androidseminar2026.ViewModel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.uistate.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BudgetViewModel @Inject constructor(private val store: DataStore<Preferences>) : ViewModel() {
    private val budgetKey = longPreferencesKey("amount_won")
    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState = _uiState.asStateFlow()
    private var observation: Job? = null

    init { observeBudget() }

    fun observeBudget() {
        observation?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        observation = viewModelScope.launch {
            try {
                store.data.collect { preferences ->
                    _uiState.update { it.copy(amount = preferences[budgetKey], isLoading = false) }
                }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "예산을 읽지 못했어요. 다시 시도해 주세요.") }
            }
        }
    }

    fun save(input: String, onSaved: () -> Unit) {
        val text = input.trim()
        val amount = text.toLongOrNull()
        if (text.isEmpty() || text.any { it !in '0'..'9' } || amount == null) {
            _uiState.update { it.copy(errorMessage = "예산은 0 이상의 정수로 입력해 주세요.") }
            return
        }
        writeBudget(amount, onSaved)
    }

    fun clear(onSaved: () -> Unit) = writeBudget(null, onSaved)

    private fun writeBudget(amount: Long?, onSaved: () -> Unit) {
        if (_uiState.value.isSaving) return
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                store.edit { if (amount == null) it.remove(budgetKey) else it[budgetKey] = amount }
                _uiState.update { it.copy(amount = amount) }
                onSaved()
            } catch (e: IOException) {
                _uiState.update { it.copy(errorMessage = "예산을 저장하지 못했어요. 다시 시도해 주세요.") }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
