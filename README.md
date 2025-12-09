<img width="3465" height="1331" alt="image" src="https://github.com/user-attachments/assets/5a6c1965-fe2d-48cf-9530-818aced889d4" /># 🐾 7Hours: 반려견 산책로 공유 및 모니터링

**7Hours**는 반려견과의 산책을 더욱 특별하게 만들어주는 안드로이드 애플리케이션입니다. 직접 걸은 산책로를 기록하고 다른 사용자들과 공유하며, 원격으로 반려견의 상태를 모니터링하는 등 반려 생활을 풍요롭게 하는 다양한 기능을 제공합니다.
[발표링크](https://docs.google.com/presentation/d/1m5UmF2lI8zsu2avCn9C5CNhvae_siHfdrOsQWkSA4zk/edit?usp=sharing)

## ✨ 주요 기능 상세

### 🔐 인증 (Authentication)
- **역할**: 소셜/이메일 로그인, 회원가입, 세션 관리를 담당합니다.
- **주요 로직**:
  - **세션 관리**: 사용자가 로그인하면 서버로부터 Access/Refresh 토큰을 발급받아 암호화된 `DataStore`에 저장합니다.
  - **자동 로그인**: 앱 실행 시 `SplashActivity`에서 `DataStore`의 토큰 유효성을 검사하여 자동 로그인을 처리합니다.
  - **토큰 갱신**: API 통신 시 Access 토큰이 만료(401 Unauthorized)되면 `OkHttp Authenticator`가 이를 감지하여 Refresh 토큰으로 새로운 Access 토큰을 자동 발급받고, 실패한 요청을 재시도합니다.
  - **CSRF 방어**: `OkHttp Interceptor`를 통해 모든 API 요청 헤더에 `X-CSRF-Token`을 추가하여 CSRF 공격을 방어합니다.

### 🗺️ 산책로 (Trail)
- **역할**: 산책로 기록, 저장, 탐색 및 경로 안내 기능을 제공합니다.
- **주요 로직**:
  - **경로 기록**: `ForegroundService`(`CurrentLocationService`)를 사용하여 앱이 백그라운드에 있을 때도 사용자의 GPS 좌표를 안정적으로 수집합니다. 수집된 좌표(`LatLng`) 리스트는 `ViewModel`에 `StateFlow`로 전달됩니다.
  - **경로 렌더링**: `ViewModel`의 좌표 리스트를 구독하여 Naver Maps SDK의 `PolylineOverlay`를 통해 지도 위에 실시간으로 이동 경로를 그립니다.
  - **주변 탐색**: 사용자의 현재 위치를 기준으로 서버에 주변 산책로 데이터를 요청하고, 클러스터링하여 지도에 마커로 표시합니다.

### 👀 원격 펫 모니터링 (Monitoring)
- **역할**: WebRTC와 GPS를 이용해 원격으로 펫을 실시간 모니터링합니다.
- **주요 로직 (WebRTC)**:
  1.  **역할 분담**: 모니터링 화면 진입 시, `ViewModel`이 사용자 계정 타입을 확인하여 '주인(Owner)' 또는 '펫(Pet)' 역할을 부여합니다.
  2.  **연결 시작 (Offer)**: '주인'이 모니터링을 시작하면 `WebRTCUseCase`를 통해 P2P 연결을 제안하는 `Offer` SDP를 생성하고, 시그널링 서버(백엔드)를 통해 '펫'에게 전송합니다.
  3.  **연결 응답 (Answer)**: '펫' 디바이스는 `Offer`를 수신한 뒤, 연결을 수락하는 `Answer` SDP를 생성하여 '주인'에게 응답합니다.
  4.  **P2P 연결 수립**: `Offer`/`Answer` 교환 후, 두 디바이스는 각자의 네트워크 정보를 담은 `ICE Candidate`를 교환하여 서버 중계 없이 직접 통신할 수 있는 P2P 경로를 찾습니다.
  5.  **스트리밍**: P2P 연결이 완료되면 '펫' 디바이스의 카메라(`localVideoTrack`) 영상이 '주인' 디바이스(`remoteVideoTrack`)로 실시간 전송됩니다.

### 🤝 커뮤니티 (Community)
- **역할**: 사용자들이 자유롭게 게시물을 작성하고 소통하는 공간을 제공합니다.
- **주요 로직**:
  - **피드**: `LazyColumn`을 사용하여 서버로부터 받은 게시물 목록을 효율적으로 표시합니다.
  - **상호작용**: 각 게시물(`PostCardView`)에 대해 좋아요, 댓글 달기 등 사용자 상호작용 기능을 제공합니다.
  - **게시물 관리**: 사용자는 `PostEditorDialogView`를 통해 새로운 게시물을 작성하거나 자신의 기존 게시물을 수정/삭제할 수 있습니다.

### 👤 마이페이지 & AI 요약 (MyPage & AI)
- **역할**: 사용자 정보 관리, 캘린더 기반 활동 기록 조회, AI 산책 일지 요약을 제공합니다.
- **주요 로직**:
  - **캘린더**: 사용자의 월별 산책 기록을 `Room` DB에서 조회하여 캘린더 UI에 표시합니다.
  - **AI 요약**: 사용자가 특정 날짜의 산책 기록을 선택하면, 해당 날짜의 산책 데이터(경로, 시간, 거리 등)를 백엔드에 전송합니다. 백엔드의 **Ollama(Llama 3)** 모델이 이 데이터를 바탕으로 자연어 형태의 산책 요약 일지를 생성하여 반환합니다.

## 🛠️ 기술 스택 및 라이브러리

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **아키텍처**: Multi-module Clean Architecture
- **비동기 처리**: Coroutines, Flow
- **의존성 주입**: Hilt
- **네트워킹**: Retrofit
- **실시간 통신**: WebRTC
- **지도**: Naver Maps SDK
- **로컬 DB**: Room
- **AI**: Ollama

## 🏗️ 아키텍처

본 프로젝트는 유지보수성과 확장성을 고려하여 **클린 아키텍처**를 기반으로 설계되었습니다. 각 레이어와 모듈은 독립적인 역할을 수행하며, 의존성 규칙을 엄격히 준수합니다.

자세한 내용은 각 모듈의 `README.md` 문서를 참고해주세요.

- **`app`**: [모듈 조립 및 UI 진입점](./app/README.md)
- **`domain`**: [핵심 비즈니스 로직](./domain/README.md)
- **`data`**: [데이터 소스 및 저장소 구현](./data/README.md)
- **Features**
  - **`feature/common`**: [공통 UI 및 로직](./feature/common/README.md)
  - **`feature/trail`**: [산책로 기능](./feature/trail/README.md)
  - **`feature/monitor`**: [모니터링 기능](./feature/monitor/README.md)
  - **`feature/community`**: [커뮤니티 기능](./feature/community/README.md)
  - **`feature/mypage`**: [마이페이지 기능](./feature/mypage/README.md)
  - **`feature/auth`**: [인증 기능](./feature/auth/README.md)
  - **`feature/home`**: [홈 화면](./feature/home/README.md)

> 더 상세한 아키텍처 구조는 [architecture](./architecture.md) 문서에서 확인할 수 있습니다.
