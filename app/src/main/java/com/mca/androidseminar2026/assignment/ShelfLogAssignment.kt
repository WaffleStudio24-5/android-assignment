package com.mca.androidseminar2026.assignment

import com.mca.androidseminar2026.model.SaveReviewResult
import com.mca.androidseminar2026.model.ContentListItemUiModel

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

    fun search(query: String): List<ContentListItemUiModel> {
        return contents
            .filter { content ->
                if (query.isEmpty()) {
                    true
                } else {
                    when (content) {
                        is Content.Book ->
                            content.title.contains(query, ignoreCase = true) ||
                                    content.author.contains(query, ignoreCase = true)

                        is Content.Movie ->
                            content.title.contains(query, ignoreCase = true) ||
                                    content.director.contains(query, ignoreCase = true)
                    }
                }
            }
            .sortedBy { it.id }
            .map { content ->
                when (content) {
                    is Content.Book -> {
                        ContentListItemUiModel(
                            id = content.id,
                            typeLabel = "책",
                            title = content.title,
                            creator = content.author,
                            year = content.year,
                            pageCount = content.pageCount,
                            runningTimeMinutes = null,
                            reviewSummary = content.review?.let {
                                if (it.memo.isEmpty()) {
                                    "평점: ${it.rating}"
                                } else {
                                    "평점: ${it.rating} / ${it.memo}"
                                }
                            } ?: "",
                        )
                    }

                    is Content.Movie -> {
                        ContentListItemUiModel(
                            id = content.id,
                            typeLabel = "영화",
                            title = content.title,
                            creator = content.director,
                            year = content.year,
                            pageCount = null,
                            runningTimeMinutes = content.runningTimeMinutes,
                            reviewSummary = content.review?.let {
                                if (it.memo.isEmpty()) {
                                    "평점: ${it.rating}"
                                } else {
                                    "평점: ${it.rating} / ${it.memo}"
                                }
                            } ?: "",
                        )
                    }
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

        val content = contents.find { it.id == contentId }
            ?: return SaveReviewResult.Failure

        content.review = Review(
            rating = rating,
            memo = memo,
        )

        return SaveReviewResult.Success
    }

    fun clearReviews() {
        contents.forEach { content ->
            content.review = null
        }
    }
}
