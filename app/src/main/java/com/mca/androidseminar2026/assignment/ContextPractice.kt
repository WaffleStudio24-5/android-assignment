package com.mca.androidseminar2026.assignment

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

object ContextPractice {
    fun showSavedToast(
        activityContext: Context,
        applicationContext: Context,
    ) {
        // 선택한 Context: applicationContext
        // 화면에 붙을 필요가 없는 짧은 알림이므로 Activity 수명과 무관한 Context가 알맞습니다.
        Toast.makeText(
            applicationContext,
            "기록을 저장했어요.",
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun showClearConfirmation(
        activityContext: Context,
        applicationContext: Context,
        onConfirmed: () -> Unit,
    ) {
        // 선택한 Context: activityContext
        // Dialog는 현재 화면의 창 위에 표시되어야 하므로 Activity Context가 필요합니다.
        AlertDialog.Builder(activityContext)
            .setTitle("전체 기록 삭제")
            .setMessage("저장된 모든 감상 기록을 삭제할까요?")
            .setNegativeButton("취소", null)
            .setPositiveButton("삭제") { _, _ -> onConfirmed() }
            .show()
    }
}