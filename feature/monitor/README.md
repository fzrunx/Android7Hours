# Feature-Monitor Module

`feature/monitor` 모듈은 등록된 펫 디바이스를 원격으로 모니터링하는 기능을 담당하는 프레젠테이션 레이어 모듈입니다. WebRTC를 이용한 실시간 영상 스트리밍과 GPS를 이용한 위치 추적 기능을 제공합니다.

## 주요 기능 및 화면

이 모듈은 사용자의 역할('주인' 또는 '펫')에 따라 다른 UI 흐름을 가집니다.

### 주인(Owner)용 기능

- **펫 선택 (`PetSelectionScreen`)**
  - 모니터링할 펫의 목록을 보여주고, 사용자가 펫을 선택할 수 있도록 합니다.

- **모니터링 대시보드 (`MonitorMainScreen` -> `MonitoringDashboard`)**
  - 펫 선택 후 진입하는 메인 화면입니다.
  - 상단의 탭을 통해 'Webcam'과 'GPS' 모니터링 방식을 전환할 수 있습니다.

- **Webcam 모니터링 (`MonitorCamScreen`)**
  - WebRTC를 통해 펫 디바이스의 카메라 영상을 실시간으로 수신하여 화면에 보여줍니다.

- **GPS 모니터링 (`MonitorGpsScreen`)**
  - 펫 디바이스로부터 수신한 GPS 좌표를 Naver Map 위에 표시하여 펫의 현재 위치를 실시간으로 보여줍니다.

### 펫(Pet)용 기능

- **스트리밍 대기 및 송출 (`MonitorCamScreen`)**
  - 펫 디바이스로 지정된 경우, 앱 진입 시 바로 이 화면으로 시작됩니다.
  - 주인의 연결 요청(Offer)을 대기하며, 연결이 수립되면 디바이스의 카메라 영상을 WebRTC를 통해 송출합니다.

## 구조

- `presentation`: UI와 관련된 모든 코드를 포함합니다.
  - `ui`: `MonitorMainScreen`, `MonitorCamScreen` 등 각 화면을 구성하는 메인 Composable 함수들을 포함합니다.
  - `MonitorViewModel.kt`: 사용자의 역할(주인/펫)을 확인하고, 상태에 따라 WebRTC 세션 관리, 데이터 요청 등 모니터링 기능과 관련된 핵심 비즈니스 로직을 수행합니다.
- `nav_graph`: `app` 모듈의 `AppNavHost`에 통합될 `monitor` 기능의 Navigation Graph를 정의합니다.

## 주요 의존성

- **WebRTC**: 실시간 영상 스트리밍
- **Naver Maps SDK**: GPS 위치 표시
- **`feature/common`**: 공통 UI 컴포넌트, WebRTC 관련 UseCase 사용
- **`domain`**: 펫 정보 조회, 위치 정보 조회 등 관련 `UseCase` 호출
- **Hilt**: `MonitorViewModel` 의존성 주입
