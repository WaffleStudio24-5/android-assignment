package com.mca.androidseminar2026

import com.mca.androidseminar2026.ViewModel.OrderViewModel
import org.junit.Assert.*
import org.junit.Test

class OrderViewModelTest {
    @Test
    fun addingCombinationUpdatesQuantitiesAndPrice() {
        val vm = OrderViewModel()
        vm.increase(1)
        vm.increase(3)
        vm.addCombination(mapOf(1 to 1, 3 to 2))
        assertEquals(mapOf(1 to 2, 3 to 3), vm.uiState.value.quantities)
        assertEquals(5, vm.uiState.value.totalQuantity)
        assertEquals(19_900L, vm.uiState.value.totalPrice)
    }

    @Test
    fun saveResultControlsDialogWithoutChangingCart() {
        val vm = OrderViewModel()
        vm.increase(1)
        vm.showSaveDialog()
        vm.onSaveResult("중복")
        assertTrue(vm.uiState.value.isSaveDialogOpen)
        assertEquals("중복", vm.uiState.value.saveErrorMessage)
        vm.onSaveResult(null)
        assertFalse(vm.uiState.value.isSaveDialogOpen)
        assertEquals(mapOf(1 to 1), vm.uiState.value.quantities)
    }
}
