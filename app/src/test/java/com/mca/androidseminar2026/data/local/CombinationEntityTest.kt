package com.mca.androidseminar2026.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class CombinationEntityTest {
    @Test
    fun quantities_roundTrip_keepsOnlyPositiveEntries() {
        val quantities = mapOf(3 to 2, 1 to 1, 4 to 0)

        assertEquals(mapOf(1 to 1, 3 to 2), quantities.toQuantitiesText().toQuantities())
    }
}
