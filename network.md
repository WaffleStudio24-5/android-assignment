# 추천 네트워크 기능

`RecommendationScreen → RecommendationViewModel → RecommendationRepository → RecommendationRemoteDataSource` 순서로 추천을 불러옵니다. Repository는 Room 캐시를 먼저 관찰하고, 원격 갱신이 성공했을 때만 목록 전체와 갱신 시각을 한 트랜잭션으로 교체합니다.

- Fake와 실제 Retrofit 구현은 `RecommendationDataSourceModule`의 바인딩 한 줄로 교체합니다.
- 마지막 성공 시각이 `null`이면 캐시가 한 번도 성공하지 않은 상태입니다. 시각이 있고 목록이 비어 있으면 정상적인 빈 응답을 저장한 상태입니다.
- 기존 목록이 있을 때 실패하면 목록은 유지하고 실패 문구와 새로고침 버튼을 보여 줍니다. 캐시가 없을 때 실패하면 오류와 재시도만 보여 줍니다.
- Logcat 태그는 `RecommendationNetwork`입니다. 요청 메서드·URL, 응답 코드·시간 또는 예외·시간을 확인할 수 있습니다.

Fake 확인 순서: 실패 → 재시도 성공 → 갱신 실패(캐시 유지) → 다른 목록으로 교체 → 빈 목록 성공 → 빈 목록 유지 실패 → 다시 성공을 확인합니다.
