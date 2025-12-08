# Feature-Trail Module

`feature/trail` 모듈은 산책로 생성, 탐색, 공유 등 앱의 핵심적인 산책로 관련 기능을 담당하는 프레젠테이션 레이어 모듈입니다.

## 주요 기능 및 화면

- **산책로 메인 (`TrailMainScreen`)**
  - Naver Map을 기반으로 사용자의 현재 위치와 주변에 등록된 산책로들을 표시합니다.
  - 사용자는 지도를 통해 주변 산책로를 탐색할 수 있습니다.
  - 산책로 생성을 시작하는 진입점 역할을 합니다.

- **산책로 생성 (`TrailCreateScreen`)**
  - 사용자가 반려견과 산책하는 동안 이동 경로를 실시간으로 기록합니다.
  - `CurrentLocationService`를 통해 백그라운드에서도 위치 정보를 수집합니다.
  - 기록 완료 후, 산책로의 이름, 사진, 메모 등을 추가하여 서버에 저장합니다.

- **산책로 상세정보 (`TrailDetailScreen`)**
  - 사용자가 선택한 산책로의 전체 경로(Polyline), 거리, 예상 시간, 등록된 사진 등을 상세하게 보여줍니다.
  - 다른 사용자가 남긴 후기나 코멘트를 확인할 수 있습니다.

- **장소 상세정보 (`PlaceInfoDetailScreen`)**
  - 산책로 내에 등록된 특정 장소(POI)에 대한 상세 정보를 제공합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `TrailMainScreen`, `TrailCreateScreen` 등 각 화면을 구성하는 메인 Composable 함수들을 포함합니다.
  - `component`: `trail` 기능 내에서 재사용되는 작은 단위의 Composable들을 포함합니다.
  - `TrailViewModel.kt`: `trail` 기능과 관련된 데이터 상태를 관리하고, `domain` 레이어의 `UseCase`를 호출하여 비즈니스 로직을 수행합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `trail` 기능의 Navigation Graph를 정의합니다. 화면 간의 이동 로직과 인수 전달을 담당합니다.
- `utils`: `trail` 기능 내에서만 사용되는 유틸리티 함수를 포함합니다.

## 주요 의존성

- **Naver Maps SDK**: 지도 표시 및 경로(Polyline) 렌더링
- **`feature/common`**: 공통 UI 컴포넌트, 테마, 지도 관련 유틸리티 사용
- **`domain`**: 산책로 관련 `UseCase` 호출
- **Hilt**: `TrailViewModel` 의존성 주입
