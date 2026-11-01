package com.mca.androidseminar2026.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "budget_preferences")

/** DataStore를 사용하여 한 끼 예산 잔액을 영구 저장합니다. (기본값: 50,000원) */
@Singleton
class BudgetDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val keyBudget = intPreferencesKey("target_budget")

    val budgetFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[keyBudget] ?: 50000
    }

    suspend fun setBudget(amount: Int) {
        context.dataStore.edit { preferences ->
            preferences[keyBudget] = amount
        }
    }
}
