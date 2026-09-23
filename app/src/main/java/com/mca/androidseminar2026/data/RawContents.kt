package com.mca.androidseminar2026.data

/**
 * 서버나 파일에서 막 받아 온 것처럼, 아직 앱의 모델이 되지 않은 원시 데이터입니다.
 * 모든 값은 String이고 책과 영화의 필드도 다릅니다.
 */
object RawContents {

    val items: List<Map<String, String>> = listOf(
        mapOf("id" to "1", "kind" to "book", "title" to "The Alchemist", "author" to "Paulo Coelho", "year" to "1988", "pageCount" to "208"),
        mapOf("id" to "2", "kind" to "movie", "title" to "Interstellar", "director" to "Christopher Nolan", "year" to "2014", "runningTimeMinutes" to "169"),
        mapOf("id" to "3", "kind" to "movie", "title" to "Inception", "director" to "Christopher Nolan", "year" to "2010", "runningTimeMinutes" to "148"),
        mapOf("id" to "4", "kind" to "book", "title" to "The Great Gatsby", "author" to "F. Scott Fitzgerald", "year" to "1925", "pageCount" to "180"),
        mapOf("id" to "5", "kind" to "book", "title" to "Project Hail Mary", "author" to "Andy Weir", "year" to "2021", "pageCount" to "496"),
        mapOf("id" to "6", "kind" to "movie", "title" to "The Truman Show", "director" to "Peter Weir", "year" to "1998", "runningTimeMinutes" to "103"),
        mapOf("id" to "7", "kind" to "book", "title" to "Pride and Prejudice", "author" to "Jane Austen", "year" to "1813", "pageCount" to "432"),
        mapOf("id" to "8", "kind" to "movie", "title" to "Dune: Part Two", "director" to "Denis Villeneuve", "year" to "2024", "runningTimeMinutes" to "166"),
        mapOf("id" to "9", "kind" to "movie", "title" to "Soul", "director" to "Pete Docter", "year" to "2020", "runningTimeMinutes" to "100"),
        mapOf("id" to "10", "kind" to "book", "title" to "Nineteen Eighty-Four", "author" to "George Orwell", "year" to "1949", "pageCount" to "328"),
    )
}
