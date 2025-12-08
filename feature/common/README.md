# Feature-Common Module

`feature/common` 모듈은 여러 `feature` 모듈에서 공통으로 사용되는 UI 컴포넌트, 유틸리티, 리소스 등을 모아놓은 공유 라이브러리 모듈입니다. 코드 중복을 방지하고 일관된 사용자 경험을 제공하는 것을 목표로 합니다.

## 역할

- **공통 UI 컴포넌트 제공**: 커스텀 버튼, 탭, 다이얼로그, 지도 관련 컴포넌트 등 여러 화면에서 재사용되는 Jetpack Compose 함수를 제공합니다.
- **UI 테마 및 리소스 관리**: 앱의 전체적인 디자인 시스템(Color, Typography, Shape)과 공용 리소스(String, Drawable)를 관리합니다.
- **공통 UI 상태 정의**: `UiState`와 같이 UI 레이어에서 공통으로 사용되는 상태 클래스를 정의합니다.
- **유틸리티 함수 제공**: 권한 처리, 날짜/시간 포맷팅 등 프레젠테이션 레이어에서 필요한 각종 유틸리티 함수를 제공합니다.
- **백그라운드 서비스 정의**: `CurrentLocationService`와 같이 앱 전역에서 사용될 수 있는 안드로이드 서비스를 포함합니다.
- **공유 비즈니스 로직(WebRTC)**: WebRTC와 같이 여러 기능(모니터링 등)에서 사용되지만, 프레젠테이션 레이어와 밀접하게 연관된 복잡한 로직을 `UseCase`와 `Repository` 형태로 캡슐화하여 제공합니다.

## 구조

- `component`: 여러 `feature`에서 재사용 가능한 Composable 함수들을 포함합니다.
- `ui`: 앱의 디자인 시스템을 정의합니다.
  - `theme`: Color, Typography, Shape 등 앱의 테마를 정의합니다.
  - `res`: 여러 모듈에서 공용으로 사용하는 String, Drawable, Color 등의 리소스를 포함합니다.
- `ui_state`: UI 레이어에서 사용되는 전역 상태 객체(ex: `MonitorUiState`)를 정의합니다.
- `utils`: 포맷터, 권한 요청 등과 같은 UI 관련 헬퍼 클래스 및 함수를 포함합니다.
- `service`: `CurrentLocationService` 등 백그라운드에서 동작하는 서비스를 정의합니다.
- `usecase` / `repository`: WebRTC와 같이 프레젠테이션 로직과 강하게 결합된 공통 기능의 `UseCase`와 `Repository`를 포함합니다. 이는 `Domain` 레이어의 순수 비즈니스 로직과 분리하기 위함입니다.
- `model`: `webrtc`와 같이 공통 모듈 내부에서 사용되는 UI 모델을 정의합니다.

## 의존성 규칙

- `feature/common`은 `domain`과 `data` 모듈에 의존할 수 있습니다.
- 다른 `feature` 모듈들은 `feature/common`을 `implementation`하여 공통 UI 및 로직을 사용합니다.
- `feature/common`은 다른 `feature` 모듈에 의존해서는 안 됩니다.
