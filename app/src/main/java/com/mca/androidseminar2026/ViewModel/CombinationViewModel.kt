package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.data.local.CombinationEntity
import com.mca.androidseminar2026.data.local.toQuantities
import com.mca.androidseminar2026.data.local.toQuantitiesText
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val dao: CombinationDao,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CombinationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dao.observeAll().collectLatest { entities ->
                _uiState.update { it.copy(combinations = entities.map { entity ->
                    Combination(entity.id, entity.name, entity.quantitiesText.toQuantities())
                }) }
            }
        }
    }

    fun save(name: String, quantities: Map<Int, Int>, onResult: (String?) -> Unit) {
        val trimmedName = name.trim()
        when {
            trimmedName.length !in 1..20 -> onResult("이름은 1~20자로 입력해 주세요.")
            quantities.values.none { it > 0 } -> onResult("장바구니가 비어 있어요.")
            else -> viewModelScope.launch {
                val insertedId = dao.insert(CombinationEntity(name = trimmedName, quantitiesText = quantities.toQuantitiesText()))
                onResult(if (insertedId == -1L) "같은 이름의 조합이 이미 있어요." else null)
            }
        }
    }

    fun requestDelete(combination: Combination) {
        _uiState.update { it.copy(pendingDelete = combination) }
    }

    fun cancelDelete() {
        _uiState.update { it.copy(pendingDelete = null) }
    }

    fun confirmDelete() {
        val id = _uiState.value.pendingDelete?.id ?: return
        viewModelScope.launch {
            dao.deleteById(id)
            cancelDelete()
        }
    }
}
