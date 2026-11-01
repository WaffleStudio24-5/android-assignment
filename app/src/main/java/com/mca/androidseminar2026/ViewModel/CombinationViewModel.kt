package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.uistate.CombinationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** DAO를 사용해 조합을 저장·조회·삭제하고 화면 상태를 관리합니다. */
@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val dao: CombinationDao,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CombinationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllCombinations().collect { list ->
                val combinations = list.map { it.toDomainModel() }
                _uiState.update { it.copy(combinations = combinations) }
            }
        }
    }

    suspend fun saveCombination(name: String, quantities: Map<Int, Int>): Result<Unit> {
        val trimmed = name.trim()
        if (trimmed.isEmpty() || trimmed.length > 20) {
            return Result.failure(IllegalArgumentException("조합 이름은 1~20자로 입력해 주세요."))
        }
        val existing = dao.getCombinationByName(trimmed)
        if (existing != null) {
            return Result.failure(IllegalStateException("이미 존재하는 조합 이름입니다."))
        }
        dao.insertCombinationWithItems(trimmed, quantities)
        return Result.success(Unit)
    }

    fun deleteCombination(id: Long) {
        viewModelScope.launch {
            dao.deleteCombinationById(id)
        }
    }
}
