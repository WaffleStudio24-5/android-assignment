package com.mca.androidseminar2026.seminar4

import javax.inject.Inject
import kotlinx.coroutines.delay
import retrofit2.http.GET

data class Task(val id: Long, val title: String, val description: String?, val isCompleted: Boolean)

data class TaskDto(val userId: Long, val id: Long, val title: String, val completed: Boolean)

/** 서버의 completed를 앱의 isCompleted로 옮깁니다. */
fun TaskDto.toTask() = Task(id, title, null, completed)

interface TaskApi {
    /** JSONPlaceholder의 할 일 목록을 GET으로 요청합니다. */
    @GET("todos")
    suspend fun getTasks(): List<TaskDto>
}

interface TaskRemoteDataSource {
    /** 출처와 관계없이 앱 모델 목록을 반환합니다. */
    suspend fun getTasks(): List<Task>
}

enum class FakeTaskScenario { SUCCESS, EMPTY, FAILURE }

class FakeTaskRemoteDataSource @Inject constructor() : TaskRemoteDataSource {
    // 발표 중 이 값으로 성공·빈 목록·연결 오류를 선택합니다.
    private val scenario = FakeTaskScenario.SUCCESS

    /** 서버 없이 지연·목록·오류 상태를 재현합니다. */
    override suspend fun getTasks(): List<Task> {
        delay(1_000)
        return when (scenario) {
            FakeTaskScenario.SUCCESS -> listOf(
                Task(1L, "세미나 복습", null, false),
                Task(2L, "실습 완료", null, true),
            )
            FakeTaskScenario.EMPTY -> emptyList()
            FakeTaskScenario.FAILURE -> throw java.io.IOException("발표용 연결 실패")
        }
    }
}

class RetrofitTaskRemoteDataSource @Inject constructor(private val api: TaskApi) : TaskRemoteDataSource {
    /** 실제 HTTP 요청 결과를 앱 모델로 변환합니다. */
    override suspend fun getTasks(): List<Task> = api.getTasks().map { it.toTask() }
}

class TaskRepository @Inject constructor(private val remote: TaskRemoteDataSource) {
    /** 구체적인 원격 구현을 알지 않고 목록을 요청합니다. */
    suspend fun getTasks(): List<Task> = remote.getTasks()
}
