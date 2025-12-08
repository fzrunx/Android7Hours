# Feature-Community Module

`feature/community` 모듈은 사용자들이 반려견 관련 정보와 일상을 공유하는 소셜 기능을 담당하는 프레젠테이션 레이어 모듈입니다. 인스타그램과 유사한 피드 형태의 UI를 통해 사용자 간의 소통을 돕습니다.

## 주요 기능 및 화면

- **커뮤니티 메인 (`CommunityMainScreen`)**
  - 서버에 등록된 모든 게시물을 시간순으로 보여주는 메인 피드 화면입니다.
  - `LazyColumn`을 사용하여 효율적으로 게시물 목록을 렌더링합니다.
  - 게시물 작성, 수정, 삭제 등의 기능을 시작하는 진입점을 제공합니다.

- **게시물 카드 (`PostCardView`)**
  - 단일 게시물을 표시하는 재사용 가능한 Composable입니다.
  - 게시물 내용, 이미지, 작성자 정보, 좋아요 수, 댓글 수 등을 포함합니다.
  - 좋아요 버튼, 댓글 보기 등 게시물과 상호작용하는 기능을 포함합니다.

- **게시물 작성/수정 (`PostEditorDialogView`)**
  - 사용자가 새로운 게시물을 작성하거나 기존 게시물을 수정할 수 있는 다이얼로그 또는 전체 화면 UI를 제공합니다.
  - 텍스트 입력, 이미지 첨부 등의 기능을 포함합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `CommunityMainScreen` 등 각 화면을 구성하는 메인 Composable 함수들을 포함합니다.
  - `component`: 게시물 카드(`PostCardView`)와 같이 `community` 기능 내에서 재사용되는 Composable들을 포함합니다. (참고: `PostCardView.kt`는 현재 `ui`에 있지만 의미상으로는 컴포넌트입니다).
  - `CommunityViewModel.kt`: 게시물 목록 조회, 게시물 생성/수정/삭제, 좋아요, 댓글 등 커뮤니티 기능과 관련된 비즈니스 로직을 `domain`의 `UseCase`를 통해 수행합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `community` 기능의 Navigation Graph를 정의합니다. 게시물 상세 화면 등으로의 이동 로직을 포함할 수 있습니다.

## 주요 의존성

- **Coil/Glide**: 이미지 로딩 및 표시 (추정)
- **`feature/common`**: 공통 UI 컴포넌트 및 테마 사용
- **`domain`**: 게시물, 댓글, 좋아요 관련 `UseCase` 호출
- **Hilt**: `CommunityViewModel` 의존성 주입
