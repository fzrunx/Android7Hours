# Domain Layer

Domain 레이어는 앱의 핵심 비즈니스 로직을 포함하는 영역입니다. 이 모듈은 앱의 다른 부분(Data, Presentation)으로부터 완전히 독립적이며, 순수한 Kotlin/Java로만 작성되어야 합니다. 즉, Android 프레임워크에 대한 의존성을 가지지 않습니다.

##  역할

- **비즈니스 규칙 정의**: 앱의 핵심 동작과 정책을 정의합니다.
- **데이터 모델 정의**: 앱의 핵심 데이터 구조인 Model을 정의합니다. (예: `User`, `Pet`, `Trail`)
- **Use Case 정의**: 사용자의 상호작용이나 시스템 이벤트에 대한 비즈니스 로직을 캡슐화합니다. 각 Use Case는 단일 책임을 가집니다.
- **Repository 인터페이스 정의**: 데이터 소스에 접근하는 방식을 추상화한 인터페이스를 정의합니다. 실제 구현은 Data 레이어에서 이루어집니다.

## 구조

- `src/main/java/com/sesac/domain/model`: 앱의 핵심 데이터 모델을 정의합니다. Presentation과 Data 레이어에서 공통으로 사용됩니다.
- `src/main/java/com/sesac/domain/repository`: 데이터 계층에서 구현해야 할 Repository 인터페이스를 정의합니다. Domain은 이 인터페이스에만 의존합니다.
- `src/main/java/com/sesac/domain/usecase`: 특정 비즈니스 로직을 수행하는 Use Case 클래스들을 포함합니다. ViewModel은 이 Use Case를 통해 비즈니스 로직을 실행합니다.
- `src/main/java/com/sesac/domain/result`: 데이터 요청 결과(성공, 실패, 로딩 등)를 표현하기 위한 Wrapper 클래스를 정의합니다.

## 주요 의존성 규칙

- **`domain`** 모듈은 다른 어떤 모듈에도 의존해서는 안 됩니다. (순수 Kotlin/Java 라이브러리 제외)
- **`data`** 모듈과 **`presentation(feature)`** 모듈이 **`domain`** 모듈에 의존합니다.
