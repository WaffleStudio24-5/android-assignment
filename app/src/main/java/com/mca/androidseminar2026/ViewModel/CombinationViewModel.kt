package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.data.local.CombinationEntity
import com.mca.androidseminar2026.model.Combination
import com.mca.androidseminar2026.uistate.CombinationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val dao: CombinationDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CombinationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllCombinations()
                .map { entities ->
                    entities.map { it.toCombination() }
                }
                .catch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "조합 목록을 불러오지 못했습니다."
                        )
                    }
                }
                .collect { combinations ->
                    _uiState.update {
                        it.copy(
                            combinations = combinations,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun save(
        name: String,
        quantities: Map<Int, Int>,
        onResult: (String?) -> Unit
    ) {
        if (_uiState.value.isSaving) return

        val trimmedName = name.trim()

        if (trimmedName.length !in 1..20) {
            onResult("이름은 1~20자로 입력해 주세요.")
            return
        }

        if (quantities.isEmpty() || quantities.values.any { it <= 0 }) {
            onResult("저장할 메뉴가 없습니다.")
            return
        }

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                if (dao.findByName(trimmedName) != null) {
                    onResult("이미 저장된 조합 이름입니다.")
                    return@launch
                }

                val json = JSONObject().apply {
                    quantities.forEach { (menuId, quantity) ->
                        put(menuId.toString(), quantity)
                    }
                }.toString()

                dao.insertCombination(
                    CombinationEntity(
                        name = trimmedName,
                        quantitiesJson = json
                    )
                )

                onResult(null)

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                val isDuplicate = try {
                    dao.findByName(trimmedName) != null
                } catch (error: Exception) {
                    if (error is CancellationException) throw error
                    false
                }

                onResult(
                    if (isDuplicate) {
                        "이미 저장된 조합 이름입니다."
                    } else {
                        "조합 저장에 실패했습니다."
                    }
                )
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            try {
                val deleted = dao.deleteCombination(id)

                if (deleted == 0) {
                    _uiState.update {
                        it.copy(errorMessage = "삭제할 조합을 찾지 못했습니다.")
                    }
                } else {
                    _uiState.update {
                        it.copy(errorMessage = null)
                    }
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.update {
                    it.copy(errorMessage = "조합 삭제에 실패했습니다.")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun CombinationEntity.toCombination(): Combination {
        val json = JSONObject(quantitiesJson)
        val quantities = mutableMapOf<Int, Int>()
        val keys = json.keys()

        while (keys.hasNext()) {
            val key = keys.next()
            quantities[key.toInt()] = json.getInt(key)
        }

        return Combination(
            id = id,
            name = name,
            quantities = quantities
        )
    }
}

