package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.uistate.RecommendationUiState

/** TODO: 목록·로딩·캐시·빈 상태·오류와 재시도를 표시하세요. */
@Composable
fun RecommendationScreen(
    state: RecommendationUiState,
    onRefresh: () -> Unit,
    onAddToCart: (Map<Int, Int>) -> Unit,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextButton(onClick = onBack) { Text("뒤로") }
        Text("추천 조합", style = MaterialTheme.typography.headlineLarge)
        Text("아직 추천 조합을 불러오지 않았어요.")
        Button(onClick = onRefresh) { Text("불러오기") }
        // TODO: state를 표시하고 선택한 조합의 quantities를 onAddToCart에 전달하세요.
    }
}
