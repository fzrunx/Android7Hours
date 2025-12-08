# App Module

`app` 모듈은 프로젝트의 최상단에 위치하는 애플리케이션 모듈입니다. 모든 `feature` 모듈과 `domain`, `data` 레이어를 하나로 조립하여 완전한 애플리케이션을 구성하고 실행하는 역할을 담당합니다.

## 역할

- **애플리케이션 생명주기 관리**: `@HiltAndroidApp` 어노테이션을 포함하는 `Application` 클래스(`A7HApplication`)를 통해 앱의 생명주기를 관리하고, Hilt 의존성 주입 컨테이너를 초기화합니다.
- **메인 Activity 관리**: 앱의 주 진입점인 `MainActivity`와 초기 스플래시 화면을 담당하는 `SplashActivity`를 포함합니다.
- **최상위 네비게이션 정의**: Jetpack Navigation Compose를 사용하여 앱의 전체적인 화면 흐름을 정의합니다. `AppNavHost`는 각 `feature` 모듈의 Navigation Graph를 통합하여 관리합니다.
- **의존성 조립 (UseCase)**: `domain` 레이어의 `UseCase`들을 Hilt를 통해 각 `feature` 모듈의 `ViewModel`에 주입하는 DI 모듈들을 포함합니다.

## 구조

- `di`: `domain` 모듈에 정의된 `UseCase` 인터페이스의 구현체를 ViewModel에 주입하기 위한 Hilt 모듈들을 포함합니다.
- `nav_graph`: 앱의 전체 화면 전환 로직을 담고 있는 `AppNavHost.kt`와 하단 네비게이션 바를 구성하는 `AppBottomBarItem.kt` 등을 포함합니다.
- `common`: 애플리케이션 전역에서 사용되는 클래스를 포함합니다.
  - `A7HApplication.kt`: Hilt 설정을 위한 Custom `Application` 클래스입니다.
- `MainActivity.kt`: `AppNavHost`를 호스팅하는 주 Activity입니다.
- `SplashActivity.kt`: 앱 실행 시 초기 로딩 및 라우팅(로그인 여부 확인 등)을 처리합니다.

## 의존성 규칙

- `app` 모듈은 프로젝트의 모든 `feature` 모듈과 `domain`, `data` 모듈에 의존하여 애플리케이션을 조립합니다.
- 최종적으로 실행 가능한 APK를 생성하는 모듈입니다.
