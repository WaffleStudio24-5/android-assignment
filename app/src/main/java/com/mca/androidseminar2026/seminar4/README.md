# 세미나 4 발표 예제

발표 자료의 JSONPlaceholder 할 일 목록 예제입니다. 여러분들의 이해를 돕기 위해 구현했습니다.

## 실행

`MainActivity.kt`에서 `Assignment4()`를 주석 처리하고 다음 호출의 주석을 해제합니다.

```kotlin
com.mca.androidseminar2026.seminar4.Semminar4()
```

실제 `GET https://jsonplaceholder.typicode.com/todos` 요청입니다. `NetworkModule.kt`의 `DataSourceModule.bindTaskRemoteDataSource` 파라미터 타입을 `FakeTaskRemoteDataSource`로 바꾸면 서버 없이 실행됩니다. Fake의 `scenario`로 SUCCESS, EMPTY, FAILURE를 선택합니다. 실패하면 자동으로 Fake로 바뀌지는 않습니다.

## 슬라이드와 코드

| 발표 내용 | 파일 |
|---|---|
| Task·DTO 변환, 인터페이스, Fake·실제 DataSource, Repository | TaskData.kt |
| @Inject, @Binds, @Provides, Converter, Interceptor, Retrofit | NetworkModule.kt |
| 로딩·성공·연결 실패·HTTP 실패·취소, StateFlow | TaskViewModel.kt |
| collectAsStateWithLifecycle, 로딩·오류·빈 목록·할 일 목록 | TaskScreen.kt |

## 제거

MainActivity를 기본 `Assignment4()`로 복구한 뒤 `seminar4/` 폴더 전체를 삭제합니다. 과제를 제출할 때 폴더 전체를 삭제하고 제출해주세요.
