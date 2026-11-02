package com.mca.androidseminar2026.seminar4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** 발표용 할 일 예제의 진입점이며 과제 화면과 독립적입니다. */
@Composable
fun Semminar4() {
    Scaffold { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            TaskRoute(hiltViewModel())
        }
    }
}

/** ViewModel 상태를 수집하고 재시도 이벤트를 연결합니다. */
@Composable
fun TaskRoute(viewModel: TaskViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TaskContent(state, viewModel::refresh)
}

/** 슬라이드의 로딩·오류·빈 목록·정상 목록 분기를 표시합니다. */
@Composable
fun TaskContent(state: TaskUiState, onRetry: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("세미나 4 · 할 일", style = MaterialTheme.typography.headlineMedium)
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null -> {
                Text(state.errorMessage)
                Button(onClick = onRetry) { Text("다시 시도") }
            }
            state.tasks.isEmpty() -> {
                Text("할 일이 없어요")
                Button(onClick = onRetry) { Text("새로고침") }
            }
            else -> {
                Button(onClick = onRetry) { Text("새로고침") }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.tasks, key = { it.id }) { task ->
                        Text("${if (task.isCompleted) "✓" else "○"} ${task.title}")
                    }
                }
            }
        }
    }
}
