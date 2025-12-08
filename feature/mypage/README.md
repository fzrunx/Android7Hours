# Feature-MyPage Module

`feature/mypage` 모듈은 사용자 정보, 펫 관리, 활동 기록, 설정 등 개인화된 모든 기능을 담당하는 프레젠테이션 레이어 모듈입니다.

## 주요 기능 및 화면

- **마이페이지 메인 (`MypageMainScreen`)**
  - 사용자 프로필, 산책 통계 등을 요약하여 보여주는 메인 화면입니다.
  - 월별 산책 기록을 볼 수 있는 캘린더를 포함합니다.
  - 날짜 선택 시 해당 날의 산책 기록과 함께 **Ollama AI**가 생성한 산책 요약 일지를 제공합니다.
  - 다른 상세 기능 화면으로 진입하는 네비게이션 허브 역할을 합니다.

- **프로필 상세 및 수정 (`MypageDetailScreen`)**
  - 사용자의 닉네임, 프로필 사진 등 개인 정보를 수정할 수 있습니다.

- **펫 관리 (`MypageManageScreen`, `AddPetScreen`)**
  - 등록된 펫 목록을 확인하고, 새로운 펫을 추가하거나 기존 펫의 정보를 수정/삭제합니다.

- **북마크 목록 (`MypageBookmarkScreen`)**
  - 사용자가 저장한 산책로나 커뮤니티 게시물을 모아볼 수 있습니다.

- **설정 (`MypageSettingScreen`)**
  - 알림 설정, 계정 관리, 로그아웃 등 앱의 전반적인 설정을 관리합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `MypageMainScreen` 등 각 화면을 구성하는 메인 Composable 함수들과, `ProfileHeaderView` 와 같이 화면에 종속된 컴포넌트들을 포함합니다.
  - `MypageViewModel.kt`: 사용자 정보, 펫 정보, 산책 기록, 북마크 등 마이페이지와 관련된 모든 비즈니스 로직을 `domain`의 `UseCase`를 통해 수행합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `mypage` 기능의 Navigation Graph를 정의합니다.
- `model`: 마이페이지 UI에서 사용되는 데이터 클래스를 정의합니다.
- `utils`: 마이페이지 기능 내에서 사용되는 유틸리티 함수를 포함합니다.

## 주요 의존성

- **Ollama**: AI 산책 요약 기능 사용 (추정, 백엔드 연동)
- **`feature/common`**: 공통 UI 컴포넌트 및 테마 사용
- **`domain`**: 사용자, 펫, 산책 기록, 북마크 관련 `UseCase` 호출
- **Hilt**: `MypageViewModel` 의존성 주입
