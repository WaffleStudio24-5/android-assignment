package com.mca.androidseminar2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mca.androidseminar2026.ui.Assignment2Starter
import com.mca.androidseminar2026.ui.theme.AndroidSeminar2026Theme

/** 과제 2의 앱 진입점입니다. 요구사항은 프로젝트 루트의 assignment2.md를 읽어 주세요. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSeminar2026Theme {
                // Assignment2Starter 내부에서 화면과 ViewModel을 연결하세요.
                Assignment2Starter()

                // 제 로컬 환경에 저장된 정답 코드입니다. 신경쓰실 필요 없습니다.
                // com.mca.androidseminar2026.answer.ui.FoodOrderApp()
            }
        }
    }
}
