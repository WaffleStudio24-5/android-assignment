package com.mca.androidseminar2026

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.viewModelScope
import androidx.test.platform.app.InstrumentationRegistry
import com.mca.androidseminar2026.ViewModel.BudgetViewModel
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.junit.Assert.*
import org.junit.Test

class BudgetPersistenceTest {
    /** 입력 오류를 거부하고 저장한 예산이 DataStore 재생성 후에도 남는지 확인합니다. */
    @Test
    fun validatesInputAndPersistsBudget() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val file = context.cacheDir.resolve("budget-verification-${UUID.randomUUID()}.preferences_pb")
        val firstJob = SupervisorJob()
        val secondJob = SupervisorJob()
        val store = PreferenceDataStoreFactory.create(scope = CoroutineScope(firstJob + Dispatchers.IO), produceFile = { file })
        val vm = withContext(Dispatchers.Main) { BudgetViewModel(store) }
        try {
            withTimeout(5_000) { vm.uiState.first { !it.isLoading } }
            withContext(Dispatchers.Main) { vm.save("-1") { fail("invalid input must not save") } }
            assertNotNull(vm.uiState.value.errorMessage)
            val saved = CompletableDeferred<Unit>()
            withContext(Dispatchers.Main) { vm.save(" 10000 ") { saved.complete(Unit) } }
            withTimeout(5_000) { saved.await() }
            assertEquals(10_000L, vm.uiState.value.amount)
            withContext(Dispatchers.Main) { vm.viewModelScope.cancel() }
            firstJob.cancelAndJoin()
            val reopened = PreferenceDataStoreFactory.create(scope = CoroutineScope(secondJob + Dispatchers.IO), produceFile = { file })
            assertEquals(10_000L, reopened.data.first()[longPreferencesKey("amount_won")])
        } finally {
            withContext(Dispatchers.Main) { vm.viewModelScope.cancel() }
            firstJob.cancelAndJoin()
            secondJob.cancelAndJoin()
            file.delete()
        }
    }
}
