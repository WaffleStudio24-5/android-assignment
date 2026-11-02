package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.data.RecommendationRepository
import com.mca.androidseminar2026.uistate.RecommendationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** TODO: Room 관찰 결과와 갱신 상태를 UI에 연결하세요. */
@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val repository: RecommendationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState = _uiState.asStateFlow()

    // TODO: 처음에는 캐시를 읽고 원격 갱신을 시도하세요.
    // 아직 구현되지 않은 Repository를 호출하지 않으므로 시작 코드도 실행할 수 있습니다.

    /** TODO: 중복 요청을 막고 코루틴에서 갱신·실패·취소를 처리하세요. */
    fun refresh() {
        // repository.refresh()와 UiState를 연결하세요.
    }
}
