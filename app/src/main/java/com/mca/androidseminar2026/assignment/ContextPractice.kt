package com.mca.androidseminar2026.assignment

import android.content.Context

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
        // 이 Context를 사용해야 하는 이유: 화면과 관련 없이 메모리에 내용을 저장할 것이기에
        android.widget.Toast.makeText(applicationContext, "기록이 저장되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
    }

    fun showClearConfirmation(
        activityContext: Context,
        applicationContext: Context,
        onConfirmed: () -> Unit,
    ) {
        // 선택한 Context: activityContext
        // 이 Context를 사용해야 하는 이유: 사용자가 삭제여부를 클릭할 화면을 띄울 것이기 때문에
        android.app.AlertDialog.Builder(activityContext)
            .setTitle("전체 기록 삭제")
            .setMessage("모든 작품의 감상 기록을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ -> onConfirmed() }
            .setNegativeButton("취소", null)
            .show()
    }
}
