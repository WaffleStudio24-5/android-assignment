package com.mca.androidseminar2026.assignment

import com.mca.androidseminar2026.model.SaveReviewResult
import com.mca.androidseminar2026.model.ContentListItemUiModel

private sealed interface Content {
    val id: Int
    val title: String
    val year: Int
}

private data class Book(
    override val id: Int,
    override val title: String,
    override val year: Int,
    val author: String,
    val pageCount: Int,
) : Content

private data class Movie(
    override val id: Int,
    override val title: String,
    override val year: Int,
    val director: String,
    val runningTimeMinutes: Int,
) : Content

private data class Review(
    val rating: Int,
    val memo: String,
)

class ShelfLogAssignment(rawCatalog: List<Map<String, String>>) {
    private val contents: List<Content> = rawCatalog.map { rawContent ->
        val id = rawContent.getValue("id").toInt()
        val title = rawContent.getValue("title")
        val year = rawContent.getValue("year").toInt()

        when (val kind = rawContent.getValue("kind")) {
            "book" -> Book(
                id = id,
                title = title,
                year = year,
                author = rawContent.getValue("author"),
                pageCount = rawContent.getValue("pageCount").toInt(),
            )

            "movie" -> Movie(
                id = id,
                title = title,
                year = year,
                director = rawContent.getValue("director"),
                runningTimeMinutes = rawContent.getValue("runningTimeMinutes").toInt(),
            )

            else -> error("Unsupported content kind: $kind")
        }
    }

    private val reviewsByContentId = mutableMapOf<Int, Review>()

    fun search(query: String): List<ContentListItemUiModel> {
        return contents
            .filter { content ->
                query.isEmpty() ||
                    content.title.contains(query, ignoreCase = true) ||
                    when (content) {
                        is Book -> content.author.contains(query, ignoreCase = true)
                        is Movie -> content.director.contains(query, ignoreCase = true)
                    }
            }
            .sortedBy { content -> content.id }
            .map { content ->
                val reviewSummary = reviewsByContentId[content.id]?.let { review ->
                    if (review.memo.isEmpty()) {
                        "평점 ${review.rating}점"
                    } else {
                        "평점 ${review.rating}점 · ${review.memo}"
                    }
                }.orEmpty()

                when (content) {
                    is Book -> ContentListItemUiModel(
                        id = content.id,
                        typeLabel = "책",
                        title = content.title,
                        creator = content.author,
                        year = content.year.toString(),
                        pageCount = content.pageCount,
                        runningTimeMinutes = null,
                        reviewSummary = reviewSummary,
                    )

                    is Movie -> ContentListItemUiModel(
                        id = content.id,
                        typeLabel = "영화",
                        title = content.title,
                        creator = content.director,
                        year = content.year.toString(),
                        pageCount = null,
                        runningTimeMinutes = content.runningTimeMinutes,
                        reviewSummary = reviewSummary,
                    )
                }
            }
    }

    fun saveReview(
        contentId: Int,
        ratingText: String,
        memo: String,
    ): SaveReviewResult {
        val rating = ratingText.toIntOrNull()
        if (rating == null || rating !in 1..5) {
            return SaveReviewResult.Failure
        }
        if (contents.none { content -> content.id == contentId }) {
            return SaveReviewResult.Failure
        }

        reviewsByContentId[contentId] = Review(rating = rating, memo = memo)
        return SaveReviewResult.Success
    }

    fun clearReviews() {
        reviewsByContentId.clear()
    }
}
