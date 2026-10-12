package com.mca.androidseminar2026.ViewModel

import androidx.lifecycle.ViewModel
import com.mca.androidseminar2026.data.local.CombinationDao
import com.mca.androidseminar2026.uistate.CombinationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** TODO: DAO를 사용해 조합을 저장·조회·삭제하고 화면 상태를 관리하세요. */
@HiltViewModel
class CombinationViewModel @Inject constructor(
    private val dao: CombinationDao,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CombinationUiState())
    val uiState = _uiState.asStateFlow()

    // TODO: save(name, quantities), loadToCart(id), delete(id) 등을 구현하세요.
}
