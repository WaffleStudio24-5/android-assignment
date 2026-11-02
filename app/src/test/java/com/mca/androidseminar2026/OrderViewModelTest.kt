package com.mca.androidseminar2026

import com.mca.androidseminar2026.ViewModel.OrderViewModel
import org.junit.Assert.*
import org.junit.Test

class OrderViewModelTest {
    /** 기존 수량에 조합을 더하고 합계도 같은 상태에서 계산합니다. */
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

    /** 저장 실패는 Dialog를 유지하고 성공할 때만 닫습니다. */
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
