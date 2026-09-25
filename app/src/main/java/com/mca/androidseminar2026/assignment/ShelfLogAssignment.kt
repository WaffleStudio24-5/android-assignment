package com.mca.androidseminar2026.assignment

import com.mca.androidseminar2026.model.SaveReviewResult
import com.mca.androidseminar2026.model.ContentListItemUiModel

/**
 * [과제 구현 파일 1]
 *
 * RawContents의 원시 데이터를 분석해 앱의 데이터 모델을 직접 설계하세요.
 * 필요한 data class, enum class, 인터페이스, 상속 구조, 컬렉션을 이 파일에 자유롭게 추가할 수 있습니다.
 * search, saveReview, clearReviews 함수의 형태를 수정할 때에는 해당 함수를 호출하는 곳도 함께 수정하세요.
 */
class ShelfLogAssignment(rawCatalog: List<Map<String, String>>) {
    private sealed class Content {
        abstract val id: Int
        abstract val title: String
        abstract val year: String
        abstract var review: Review?

        data class Book(
            override val id: Int,
            override val title: String,
            override val year: String,
            val author: String,
            val pageCount: Int,
            override var review: Review? = null,
        ) : Content()

        data class Movie(
            override val id: Int,
            override val title: String,
            override val year: String,
            val director: String,
            val runningTimeMinutes: Int,
            override var review: Review? = null,
        ) : Content()
    }

    private data class Review(
        val rating: Int,
        val memo: String,
    )

    // TODO 1. 원시 데이터인 rawCatalog를 앱에서 사용할 작품 목록으로 변환해 프로퍼티로 보관하세요.
    // TODO 3~5의 검색, 저장, 전체 삭제 함수가 이 프로퍼티를 사용할 수 있어야 합니다.
    // 클래스를 새로 정의해서 사용해 주세요.
    // 단, 정의하신 클래스의 id는 반드시 Int 타입으로 해 주세요.
    //
    private val contents: List<Content> = rawCatalog.map { raw ->
        when (raw["kind"]) {
            "book" -> Content.Book(
                id = raw["id"]!!.toInt(),
                title = raw["title"]!!,
                year = raw["year"]!!,
                author = raw["author"]!!,
                pageCount = raw["pageCount"]!!.toInt(),
            )

            "movie" -> Content.Movie(
                id = raw["id"]!!.toInt(),
                title = raw["title"]!!,
                year = raw["year"]!!,
                director = raw["director"]!!,
                runningTimeMinutes = raw["runningTimeMinutes"]!!.toInt(),
            )

            else -> error("Unknown content kind")
        }
    }

    // TODO 2. 작품별 감상 기록을 저장하는 방식을 결정하세요.
    // 각 기록에는 평점과 메모가 필요합니다.
    //
    // 아래 방식 중 하나를 선택하거나, 다른 방식을 사용해도 됩니다.
    // - 위에서 만든 작품 객체 안에 감상 기록을 포함한다.
    // - 작품 ID를 프로퍼티로 가지는, 감상 기록 저장용 객체를 새로 만든다.

    /** 검색 결과를 ContentListItemUiModel로 변환해 반환하세요. */
    fun search(query: String): List<ContentListItemUiModel> {
        // TODO 3. 빈 검색어일 경우엔 ContentListItemUiModel로 변환된 Catalog 전체를, 그 외에는 제목/저자 or 감독에 해당 query가 들어간 것들에 대해 결과를 반환하세요.
        return emptyList()
    }

    /** 입력을 검증하고 감상 기록을 추가하거나 기존 기록을 갱신하세요. */
    fun saveReview(
        contentId: Int,
        ratingText: String,
        memo: String,
    ): SaveReviewResult {
        TODO("TODO 4. 평점을 검증하고, 같은 작품의 기록은 갱신하세요.")
    }

    /** 모든 작품에서 감상 기록을 지우세요. */
    fun clearReviews() {
        TODO("TODO 5. 모든 작품에서 감상 기록을 지우세요.")
    }
}
