# Feature-Home Module

`feature/home` 모듈은 사용자가 앱에 진입했을 때 가장 먼저 보게 되는 메인 대시보드 화면을 담당하는 프레젠테이션 레이어 모듈입니다. 다른 주요 기능들로 빠르게 이동할 수 있는 바로가기와 요약 정보를 제공합니다.

## 주요 기능 및 화면

- **홈 스크린 (`HomeScreen`)**
  - 앱의 핵심 기능들을 요약하여 보여주는 랜딩 페이지입니다.
  - 주요 공지나 이벤트를 보여주는 배너 섹션을 포함할 수 있습니다.
  - '오늘의 추천 산책로', '인기 커뮤니티 게시물' 등 다른 기능의 콘텐츠를 카드로 보여주며, 해당 기능으로 이동하는 링크 역할을 합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `HomeScreen`과 이 화면에서 사용되는 `BannerSectionView`, `ContentCardView` 같은 Composable 함수들을 포함합니다.
  - `HomeViewModel.kt`: 홈 화면에 필요한 데이터(추천 산책로, 인기 게시물 등)를 `domain`의 `UseCase`를 통해 가져와 UI에 제공합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `home` 기능의 Navigation Graph를 정의합니다. `HomeScreen`은 앱의 시작 화면 중 하나로 설정됩니다.

## 주요 의존성

- **`feature/common`**: 공통 UI 컴포넌트 및 테마 사용
- **`domain`**: 산책로, 게시물 등 홈 화면에 표시할 데이터를 가져오기 위한 `UseCase` 호출
- **Hilt**: `HomeViewModel` 의존성 주입
