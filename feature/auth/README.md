# Feature-Auth Module

`feature/auth` 모듈은 로그인, 회원가입, 계정 찾기 등 사용자 인증과 관련된 모든 기능을 담당하는 프레젠테ATION 레이어 모듈입니다.

## 주요 기능 및 화면

- **로그인 (`AuthLoginScreen`)**
  - 이메일/비밀번호를 이용한 기본 로그인 기능을 제공합니다.
  - 구글, 카카오 등 소셜 로그인을 위한 버튼(`SocialJoinButtonView`)을 포함합니다.
  - 회원가입, 계정 찾기 화면으로 이동하는 링크를 제공합니다.

- **회원가입 (`AuthJoinScreen`)**
  - 이메일, 비밀번호, 닉네임 등을 입력받아 새로운 계정을 생성합니다.
  - 서비스 이용약관, 개인정보 처리방침 동의(`AgreementSectionView`) 절차를 포함합니다.

- **초기 프로필 설정 (`ProfileScreen`)**
  - 회원가입 직후, 사용자의 초기 프로필(사진, 소개 등)을 설정하는 화면입니다.

- **계정 찾기 (`FindAccountScreen`)**
  - 사용자가 이메일 주소나 다른 정보를 이용해 자신의 계정을 찾을 수 있도록 돕는 기능을 제공합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `AuthLoginScreen`, `AuthJoinScreen` 등 각 화면을 구성하는 메인 Composable 함수들을 포함합니다.
  - `AuthViewModel.kt`: 로그인, 회원가입, 초기 프로필 설정 등 주된 인증 로직을 `domain`의 `UseCase`를 통해 수행합니다.
  - `FindAccountViewModel.kt`: 계정 찾기 기능에 대한 비즈니스 로직을 담당합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `auth` 기능의 Navigation Graph를 정의합니다. `SplashActivity`의 로직에 따라 사용자가 로그인되어 있지 않을 경우, 이 `auth` 네비게이션 그래프가 시작점이 됩니다.

## 주요 의존성

- **Google Sign-In, Kakao SDK**: 소셜 로그인을 위해 각 플랫폼의 SDK 사용 (추정)
- **`feature/common`**: 공통 UI 컴포넌트 및 테마 사용
- **`domain`**: 사용자 인증, 세션 관리 등과 관련된 `UseCase` 호출
- **Hilt**: `AuthViewModel`, `FindAccountViewModel` 의존성 주입
