package com.mca.androidseminar2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.mca.androidseminar2026.ui.Assignment4
import com.mca.androidseminar2026.ui.theme.AndroidSeminar2026Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSeminar2026Theme {
                // FIXME 실행할 앱 선택: 아래 진입점 중 하나만 주석 해제합니다.
                // 학생 배포용
                Assignment4()

                // 답안: 여러분들의 파일에는 없으니 신경 안 쓰셔도 됩니다..
                // com.mca.androidseminar2026.answer.Assignment4AnswerRoot()

                // 세미나 4 예제
                // com.mca.androidseminar2026.seminar4.Semminar4()
            }
        }
    }
}
