package com.mca.androidseminar2026.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mca.androidseminar2026.model.ContentListItemUiModel

/** Compose 화면 구현입니다. 이 파일은 수정할 필요가 없습니다. */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
fun ShelfLogScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    results: List<ContentListItemUiModel>,
    selectedContentId: Int?,
    onContentClick: (ContentListItemUiModel) -> Unit,
    selectedContentTitle: String,
    rating: String,
    onRatingChange: (String) -> Unit,
    memo: String,
    onMemoChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onClearAllClick: () -> Unit,
) {
    Scaffold(topBar = { TopAppBar(title = { Text("ShelfLog") }) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp, padding.calculateTopPadding() + 12.dp, 20.dp, 28.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { Text("책과 영화를 찾아 감상을 남겨 보세요.", style = MaterialTheme.typography.bodyLarge) }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                        label = { Text("제목, 저자 또는 감독") },
                    )
                    Button(onClick = onSearch) { Text("검색") }
                }
            }
            item { Text("검색 결과 ${results.size}개", style = MaterialTheme.typography.titleMedium) }
            if (results.isEmpty()) item { Text("검색 결과가 없습니다.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            items(results, key = { it.id }) { row -> CatalogRow(row, row.id == selectedContentId) { onContentClick(row) } }
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp)) }
            item { Text(if (selectedContentId == null) "작품을 선택해 기록을 남겨 주세요" else "${selectedContentTitle} 기록하기", style = MaterialTheme.typography.titleMedium) }
            item {
                OutlinedTextField(rating, onRatingChange, Modifier.fillMaxWidth(), true, label = { Text("평점 (1~5)") }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
                )
            }
            item { OutlinedTextField(memo, onMemoChange, Modifier.fillMaxWidth(), label = { Text("한 줄 메모 (선택)") }, minLines = 2) }
            item { Button(onSaveClick, Modifier.fillMaxWidth(), enabled = selectedContentId != null) { Text("기록 저장") } }
            item { Button(onClearAllClick, Modifier.fillMaxWidth()) { Text("전체 기록 삭제") } }
        }
    }
}

@Composable
private fun CatalogRow(row: ContentListItemUiModel, isSelected: Boolean, onClick: () -> Unit) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = containerColor), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Text(row.typeLabel, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text(row.title, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(4.dp))
            val additionalInfo = row.pageCount?.let { "$it pages" }
                ?: row.runningTimeMinutes?.let { "$it min" }
                ?: ""
            Text("${row.creator} · ${row.year} · $additionalInfo", color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (row.reviewSummary.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(row.reviewSummary, color = Color(0xFF306A3A), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
