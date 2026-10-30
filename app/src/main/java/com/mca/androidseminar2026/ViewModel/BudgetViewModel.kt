package com.mca.androidseminar2026.ViewModel

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.uistate.BudgetUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.budgetDataStore by preferencesDataStore("budget")
private val budgetAmountKey = longPreferencesKey("amount")

@HiltViewModel
class BudgetViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : ViewModel() {
    val uiState = context.budgetDataStore.data.map { preferences ->
        BudgetUiState(amount = preferences[budgetAmountKey])
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BudgetUiState())

    fun save(amountText: String) {
        val amount = amountText.trim().toLongOrNull()
        if (amount == null || amount <= 0) return
        viewModelScope.launch {
            context.budgetDataStore.edit { it[budgetAmountKey] = amount }
        }
    }
}
