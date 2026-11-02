package com.mca.androidseminar2026.seminar4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class TaskUiState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null,
)

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    init { refresh() }

    /** 중복 요청을 막고 성공·실패·로딩 종료를 화면 상태로 전달합니다. */
    fun refresh() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val tasks = repository.getTasks()
                _uiState.update { it.copy(tasks = tasks, errorMessage = null) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.update { it.copy(errorMessage = "연결을 확인해 주세요") }
            } catch (e: HttpException) {
                _uiState.update { it.copy(errorMessage = "서버 요청에 실패했어요 (${e.code()})") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
