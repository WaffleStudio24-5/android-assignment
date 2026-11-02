package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.database.sqlite.SQLiteException
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.uistate.CombinationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 과제 3 완성 코드: Room의 저장한 조합을 화면 상태로 전달합니다. */
@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val dao: CombinationDao,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CombinationUiState())
    val uiState = _uiState.asStateFlow()

    private var observation: Job? = null

    init { observeCombinations() }

    /** DB 변경을 관찰하며 읽기 실패 후에는 다시 구독할 수 있습니다. */
    fun observeCombinations() {
        observation?.cancel()
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        observation = viewModelScope.launch {
            try {
                dao.observeAll().collect { rows ->
                    _uiState.update { it.copy(combinations = rows.map { row -> row.toModel() }, isLoading = false) }
                }
            } catch (e: SQLiteException) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "저장한 조합을 읽지 못했어요. 다시 시도해 주세요.") }
            }
        }
    }

    /** 이름을 검증하고 조합을 저장한 뒤 성공 또는 오류 문구를 돌려줍니다. */
    fun save(name: String, quantities: Map<Int, Int>, onResult: (String?) -> Unit) {
        if (_uiState.value.isSaving) return
        val trimmed = name.trim()
        if (trimmed.length !in 1..20) {
            onResult("이름은 앞뒤 공백을 제외하고 1~20자로 입력해 주세요.")
            return
        }
        if (quantities.isEmpty()) {
            onResult("장바구니에 메뉴를 먼저 담아 주세요.")
            return
        }
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try {
                val saved = dao.save(trimmed, quantities)
                onResult(if (saved) null else "같은 이름의 조합이 있어요.")
            } catch (e: SQLiteException) {
                onResult("조합을 저장하지 못했어요. 다시 시도해 주세요.")
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /** 선택한 조합을 삭제하고 실패 시 기존 목록과 오류 안내를 유지합니다. */
    fun delete(id: Long) {
        if (_uiState.value.deletingId != null) return
        _uiState.update { it.copy(deletingId = id, errorMessage = null) }
        viewModelScope.launch {
            try {
                dao.delete(id)
            } catch (e: SQLiteException) {
                _uiState.update { it.copy(errorMessage = "조합을 삭제하지 못했어요. 다시 시도해 주세요.") }
            } finally {
                _uiState.update { it.copy(deletingId = null) }
            }
        }
    }
}
