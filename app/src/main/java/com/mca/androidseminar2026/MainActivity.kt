package com.mca.androidseminar2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mca.androidseminar2026.assignment.ContextPractice
import com.mca.androidseminar2026.assignment.ShelfLogAssignment
import com.mca.androidseminar2026.data.RawContents
import com.mca.androidseminar2026.model.ContentListItemUiModel
import com.mca.androidseminar2026.model.SaveReviewResult
import com.mca.androidseminar2026.ui.ShelfLogScreen
import com.mca.androidseminar2026.ui.theme.AndroidSeminar2026Theme

/**
 * 앱과 화면을 연결하는 파일입니다. 이 파일과 ui 폴더는 수정할 필요가 없습니다.
 * 과제 구현은 assignment 폴더에서 진행합니다.
 */
class MainActivity : ComponentActivity() {
    private val assignment = ShelfLogAssignment(RawContents.items)

    private var query by mutableStateOf("")
    private var selectedContentId by mutableStateOf<Int?>(null)
    private var selectedContentTitle by mutableStateOf("")
    private var rating by mutableStateOf("")
    private var memo by mutableStateOf("")
    private var searchResults by mutableStateOf(emptyList<ContentListItemUiModel>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        refreshResults()
        setContent {
            AndroidSeminar2026Theme {
                ShelfLogScreen(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = ::refreshResults,
                    results = searchResults,
                    selectedContentId = selectedContentId,
                    onContentClick = ::selectContent,
                    selectedContentTitle = selectedContentTitle,
                    rating = rating,
                    onRatingChange = { rating = it },
                    memo = memo,
                    onMemoChange = { memo = it },
                    onSaveClick = ::saveReview,
                    onClearAllClick = ::clearAllReviews,
                )
            }
        }
    }

    private fun refreshResults() {
        searchResults = assignment.search(query)
    }

    private fun selectContent(row: ContentListItemUiModel) {
        selectedContentId = row.id
        selectedContentTitle = row.title
        rating = ""
        memo = ""
    }

    private fun saveReview() {
        val contentId = selectedContentId ?: return
        val result = assignment.saveReview(contentId, rating, memo)
        when (result) {
            SaveReviewResult.Success -> {
                ContextPractice.showSavedToast(this, applicationContext)
                refreshResults()
            }

            SaveReviewResult.Failure -> {
                android.widget.Toast.makeText(this, "평점은 1~5점으로 입력해 주세요.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearAllReviews() {
        ContextPractice.showClearConfirmation(this, applicationContext) {
            assignment.clearReviews()
            selectedContentId = null
            selectedContentTitle = ""
            rating = ""
            memo = ""
            refreshResults()
        }
    }
}
