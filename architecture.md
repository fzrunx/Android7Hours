# 🏗️ Architecture

본 프로젝트는 Google이 권장하는 앱 아키텍처 가이드를 따라, **계층형 아키텍처(Layered Architecture)**와 **모듈화(Multi-module)**를 적용하여 유지보수성과 확장성을 높이는 것을 목표로 설계되었습니다.

## 1. 기본 원칙

- **관심사의 분리 (Separation of Concerns)**: 각 레이어와 모듈은 명확하게 정의된 단일 책임을 가집니다.
- **UI 중심 설계 (UI-Driven)**: 데이터의 흐름은 최종적으로 UI를 구동하기 위해 단방향(UDF)으로 흐릅니다.
- **느슨한 결합, 높은 응집도 (Loose Coupling, High Cohesion)**: 각 컴포넌트는 독립적으로 존재하며, 인터페이스를 통해 상호작용합니다.

---

## 2. 레이어 구조 (Layered Architecture)

프로젝트는 크게 3개의 레이어로 구성되며, **의존성 규칙(Dependency Rule)**에 따라 상위 레이어가 하위 레이어에만 의존합니다. (`Presentation` → `Domain` ← `Data`)

![Architecture Diagram](https://developer.android.com/static/topic/libraries/architecture/images/mad-arch-overview.png)
*<p align="center">출처: Google Android Developers</p>*

###  nižpresentation (Feature) Layer

- **역할**: UI와 관련된 모든 로직을 처리합니다. 화면에 데이터를 표시하고 사용자 입력을 받아 처리합니다.
- **구성 요소**:
  - **`ViewModel`**: UI 상태(State)를 소유하고 비즈니스 로직을 실행합니다. `UseCase`를 통해 `Domain` 레이어와 상호작용합니다.
  - **`UI (Compose)`**: `ViewModel`이 제공하는 상태를 관찰하여 화면을 렌더링하고, 사용자 이벤트를 `ViewModel`에 전달합니다.
  - **`NavGraph`**: Jetpack Navigation Compose를 사용하여 각 `feature` 모듈 내의 화면 흐름을 정의합니다.
- **모듈**: `feature/home`, `feature/auth`, `feature/trail`, `feature/monitor` 등 기능 단위로 분리됩니다.

### 💖 Domain Layer

- **역할**: 앱의 핵심 비즈니스 로직을 포함합니다. **순수한 Kotlin**으로 작성되어 다른 레이어로부터 독립적입니다.
- **구성 요소**:
  - **`Model`**: 앱의 핵심 데이터 구조를 정의합니다.
  - **`UseCase`**: 단일 비즈니스 로직을 캡슐화합니다. (예: `GetUserUseCase`)
  - **`Repository` (Interface)**: 데이터에 접근하는 방식을 정의한 인터페이스입니다. 실제 구현은 `Data` 레이어에 위임합니다.
- **모듈**: `domain`

### 💾 Data Layer

- **역할**: `Domain` 레이어의 `Repository` 인터페이스를 구현하고, 모든 데이터 관련 작업을 처리합니다.
- **구성 요소**:
  - **`Repository` (Implementation)**: `Repository` 인터페이스의 구현체. 여러 `DataSource`를 조합하여 데이터를 가져옵니다.
  - **`DataSource`**: 데이터 소스를 나타냅니다.
    - **RemoteDataSource**: Retrofit(API), WebRTC(Signaling) 등 외부 데이터 소스.
    - **LocalDataSource**: Room(DB), DataStore 등 내부 데이터 소스.
  - **`DTO / Entity`**: 각 데이터 소스에서 사용하는 데이터 모델입니다.
  - **`Mapper`**: DTO/Entity를 `Domain`의 `Model`로 변환합니다.
- **모듈**: `data`

---

## 3. 데이터 흐름 (Unidirectional Data Flow)

사용자의 액션으로부터 UI가 업데이트되기까지 데이터는 아래와 같은 단방향 흐름을 따릅니다.

> **`UI`** → **`ViewModel`** → **`UseCase`** → **`Repository`** → **`DataSource`**
> 
> *(Data)*
> 
> **`UI`** ← **`ViewModel`** ← **`UseCase`** ← **`Repository`** ← **`DataSource`**

---

## 4. 모듈 구조

- **`app`**: 모든 모듈을 조립하고, 최상위 네비게이션과 의존성 주입(UseCase)을 설정하는 애플리케이션 모듈.
- **`feature/*`**: 각 기능 단위로 분리된 프레젠테이션 레이어 모듈. 각 `feature`는 독립적으로 작동 가능해야 합니다.
  - **`feature/common`**: 여러 `feature`에서 공통으로 사용되는 UI 컴포넌트, 리소스, 유틸리티 등을 포함하는 공유 라이브러리 모듈.
- **`domain`**: 핵심 비즈니스 로직을 담는 순수 Kotlin 라이브러리 모듈.
- **`data`**: 데이터 소스를 관리하고 `Repository`를 구현하는 Android 라이브러리 모듈.

---

## 5. 주요 기술 및 패턴

- **MVVM (Model-View-ViewModel)**: `ViewModel`이 UI 상태를 관리하고, `View(Compose)`는 이를 구독하여 화면을 그리는 패턴을 사용합니다.
- **Repository Pattern**: 데이터 소스를 추상화하여 `Domain` 레이어가 데이터 소스의 구체적인 구현에 대해 알지 못하도록 합니다.
- **DI (Dependency Injection) with Hilt**: `ViewModel`, `UseCase`, `Repository` 등 클래스 간의 의존성을 외부(Hilt)에서 주입하여 결합도를 낮추고 테스트 용이성을 높입니다.
  - `@HiltViewModel`, `@AndroidEntryPoint`
  - `@Module`, `@Provides`, `@Binds`
- **Facade Pattern**: `feature/common` 모듈의 `WebRTCUseCase`는 복잡한 WebRTC 관련 `UseCase`들을 하나로 묶어 단순화된 인터페이스를 제공함으로써, `MonitorViewModel`이 WebRTC의 내부 동작을 모두 알 필요 없이 쉽게 사용할 수 있도록 합니다.
