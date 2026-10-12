package com.mca.androidseminar2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.mca.androidseminar2026.ui.Assignment3Root
import com.mca.androidseminar2026.ui.theme.AndroidSeminar2026Theme

/** 과제 3의 앱 진입점입니다. 화면 구성은 ui 폴더에서 시작하세요. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSeminar2026Theme {
                Assignment3Root()
            }
        }
    }
}
