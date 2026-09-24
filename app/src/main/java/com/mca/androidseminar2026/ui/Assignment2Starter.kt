package com.mca.androidseminar2026.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** 앱 연결부입니다. 아래 안내 UI를 직접 구현한 화면으로 교체하세요. */
@Composable
fun Assignment2Starter() {
    // TODO: 직접 설계한 ViewModel의 상태와 OrderScreens.kt의 화면·이벤트를 연결하세요.
    // 현재 탭에 따른 화면 전환과 하단 Bottom Bar 배치도 직접 구현하세요.
    Surface(Modifier.fillMaxSize()) {
        Box(
            Modifier.safeDrawingPadding().padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("한끼 테이블\n화면의 TODO를 구현하고 상태 관리 구조를 설계하세요.")
        }
    }
}
