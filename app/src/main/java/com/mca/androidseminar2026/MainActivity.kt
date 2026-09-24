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

                // 교수자용 로컬 답안 연결: 위 호출 대신 아래 호출을 사용합니다.
                // com.mca.androidseminar2026.answer.ui.FoodOrderApp()
            }
        }
    }
}
