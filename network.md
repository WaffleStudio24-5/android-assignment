Assignment 4 — 추천 조합 네트워크 기능

1. 기능 및 데이터 흐름
추천 화면 진입 시 Room 캐시를 먼저 읽고 원격 갱신을 시도한다. 추천별 이름·설명·메뉴와 수량·전체 수량·총금액을 표시하며, 장바구니에 담기는 기존 수량에 더하는 방식이다.
RecommendationScreen → RecommendationViewModel → RecommendationRepository → RecommendationRemoteDataSource(Fake/Retrofit)
Repository는 원격 결과를 RecommendationDao로 Room에 저장한다. 
화면은 서버 응답을 직접 표시하지 않고, Room의 Flow → Repository → ViewModel → UI를 통해 갱신된다. 
Retrofit 구현은 HTTP 상태와 DTO의 필수 값·중복 ID·메뉴 ID·수량을 검증하며, 잘못된 응답은 전체 실패로 처리한다.

2. 화면 설계 및 User Flow
- 첫 진입·새로고침: 캐시가 있다면 즉시 보여 주고, 갱신 중에는 로딩 표시를 한다. 새로고침 버튼과 refreshJob으로 중복 요청을 막는다.
- 갱신 성공: 목록을 통째로 교체하고 마지막 성공 시각을 yyyy.MM.dd HH:mm 형식으로 표시한다.
- 캐시 없이 실패: 오류 안내와 다시 시도 버튼을 보여 준다.
- 캐시가 있을 때 실패: 기존 목록·갱신 시각을 유지하고 실패 안내와 재시도를 제공한다. 장바구니 담기도 계속 가능하다.
- 성공한 빈 목록: 현재 추천 조합이 없어요.를 표시한다. 이후 실패해도 빈 목록과 마지막 성공 시각을 유지한다.

3. 캐시 및 의존성 주입
기존 사용자 조합 DB와 분리된 recommendations.db에 추천 정보·메뉴별 수량·마지막 성공 시각을 저장한다. 
성공 기록이 없으면 캐시 조회 결과가 null이고, 정상적으로 빈 목록을 받았다면 성공 시각은 있으나 항목이 0개이다. 
@Transaction으로 목록과 시각을 함께 저장해 실패 시 이전 캐시를 보존한다.
Fake/Retrofit 전환은 RecommendationDataSourceModule.kt의 바인딩 구현만 변경한다. 
UI·ViewModel·Repository는 수정하지 않으며, 실제 통신 실패 시 Fake로 자동 전환하지 않는다.

4. 통신 로그
NetworkModule.kt의 OkHttp Interceptor가 X-Client: Android 헤더를 유지하고, RecommendationHTTP 태그로 요청 메서드·URL, 응답 코드·소요 시간(ms), 연결 실패 원인·소요 시간을 출력한다. 실패 예외는 다시 전달한다. 실제 요청에서 Logcat의 HTTP 200 응답을 확인했다. Fake 요청은 OkHttp를 거치지 않는다.

5. 테스트 결과
Fake 요청확인한 동작
1: 실패	캐시 없음 → 오류와 재시도
2: 기본 목록	추천 3개 표시 및 저장
3: 실패	기존 목록 유지, 오류 표시
4: 변경 목록	기존 목록 전체 교체
5: 빈 목록	추천 0개, 성공 시각 갱신
6: 실패	빈 목록·성공 시각 유지, 오류 표시
7: 기본 목록	추천 3개 복구
각 Fake 요청은 1초 지연되며, 앱 데이터를 초기화한 뒤 재빌드 없이 순서대로 확인했다. 
Retrofit으로 전환해 실제 추천 조회 및 HTTP 200 응답을 확인했고, 앱 종료 후 인터넷을 끄고 재실행해 기존 추천 표시·갱신 실패 안내·장바구니 담기를 확인했다.