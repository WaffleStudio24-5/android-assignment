package com.mca.androidseminar2026

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class StudentNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    /** 학생 시작 코드의 화면 이동과 회전 후 Navigation 복원을 확인합니다. */
    @Test
    fun recommendationsRouteSurvivesRecreation() {
        compose.onNodeWithText("장바구니").performClick()
        compose.onNodeWithText("추천 조합").performClick()
        compose.onNodeWithText("추천 조합").assertIsDisplayed()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("추천 조합").assertIsDisplayed()
        compose.onNodeWithText("뒤로").performClick()
        compose.onNodeWithText("조합 저장").assertIsDisplayed()
    }
}
