package com.mca.androidseminar2026.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.budgetDataStore by preferencesDataStore(
    name = "meal_budget"
)

class BudgetRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val budgetKey = longPreferencesKey("budget_won")

    val budget: Flow<Long?> =
        context.budgetDataStore.data.map { preferences ->
            preferences[budgetKey]
        }

    suspend fun saveBudget(amount: Long) {
        require(amount in 1L..1_000_000L)

        context.budgetDataStore.edit { preferences ->
            preferences[budgetKey] = amount
        }
    }

    suspend fun clearBudget() {
        context.budgetDataStore.edit { preferences ->
            preferences.remove(budgetKey)
        }
    }
}
