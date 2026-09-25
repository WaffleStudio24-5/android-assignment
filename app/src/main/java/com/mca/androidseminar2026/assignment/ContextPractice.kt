package com.mca.androidseminar2026.assignment

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

/**
 * [과제 구현 파일 2]
 *
 * Toast와 Dialog에 필요한 Context가 왜 다른지 생각하며 TODO를 구현하세요.
 *
 */
object ContextPractice {
    fun showSavedToast(
        activityContext: Context,
        applicationContext: Context,
    ) {
        // 선택한 Context: applicationContext
        // 이 Context를 사용해야 하는 이유:
        // Toast는 특정 Activity에 종속될 필요가 없으므로 applicationContext를 사용합니다.
        Toast.makeText(
            applicationContext,
            "기록이 저장되었습니다.",
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun showClearConfirmation(
        activityContext: Context,
        applicationContext: Context,
        onConfirmed: () -> Unit,
    ) {
        // 선택한 Context: activityContext
        // 이 Context를 사용해야 하는 이유:
        // Dialog는 현재 Activity의 화면에 표시되어야 하므로 activityContext를 사용합니다.
        AlertDialog.Builder(activityContext)
            .setTitle("전체 기록 삭제")
            .setMessage("모든 감상 기록을 삭제하시겠습니까?")
            .setNegativeButton("취소", null)
            .setPositiveButton("삭제") { _, _ ->
                onConfirmed()
            }
            .show()
    }
}