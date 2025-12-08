# Data Layer

Data 레이어는 앱의 데이터 처리와 관련된 모든 로직을 담당합니다. `Domain` 레이어에서 정의한 `Repository` 인터페이스의 구현체를 제공하며, 다양한 데이터 소스(Remote, Local)로부터 데이터를 가져오는 역할을 수행합니다.

## 역할

- **Repository 구현**: `Domain` 레이어의 `Repository` 인터페이스를 구현합니다. `UseCase`는 이 구현체를 통해 데이터에 접근하게 됩니다.
- **데이터 소스 관리**: 원격 서버(REST API, WebRTC)와 로컬 저장소(Room, DataStore) 등 다양한 데이터 소스를 관리하고, 데이터 요청을 처리합니다.
- **데이터 매핑**: DTO(Data Transfer Object)나 Entity 등 데이터 소스에서 사용하는 모델을 `Domain` 레이어의 비즈니스 모델로 변환합니다.

## 구조

- `repository`: `domain` 모듈의 `Repository` 인터페이스에 대한 실제 구현체를 포함합니다.
- `source`: 데이터 소스를 관리합니다.
  - `api`: Retrofit을 이용한 REST API 인터페이스를 정의합니다.
  - `local`: Room DB(`AppDatabase`), DataStore 등 로컬 데이터 소스를 관리합니다.
  - `webrtc`: WebRTC 시그널링 및 통신을 위한 데이터 소스를 관리합니다.
- `dto`: 원격 데이터 소스(API)로부터 받는 데이터 모델(Data Transfer Object)을 정의합니다.
- `entity`: Room 데이터베이스에서 사용하는 테이블 스키마(Entity)를 정의합니다.
- `dao`: Room 데이터베이스에 접근하기 위한 DAO(Data Access Object) 인터페이스를 정의합니다.
- `mapper`: DTO나 Entity를 `domain` 모델로 변환하거나, 그 반대의 역할을 수행하는 Mapper 클래스를 포함합니다.
- `di`: Hilt를 사용한 의존성 주입 모듈을 정의합니다.
  - `NetworkModule`: Retrofit, OkHttpClient 등 네트워크 관련 의존성을 제공합니다.
  - `DatabaseModule`: Room 데이터베이스 관련 의존성을 제공합니다.
  - `RepositoryModule`: `Repository` 구현체를 Hilt에 바인딩합니다.
- `datastore`: Jetpack DataStore를 사용하여 간단한 키-값 데이터를 저장하는 로직을 포함합니다.
- `type`: 데이터 레이어 내에서 사용되는 `enum` 클래스 등 특정 타입을 정의합니다.
- `util`: 데이터 처리와 관련된 유틸리티 함수들을 포함합니다.

## 주요 라이브러리

- **Retrofit & OkHttp**: REST API 통신
- **Room**: 로컬 데이터베이스
- **Jetpack DataStore**: 간단한 데이터 로컬 저장 (세션 토큰 등)
- **WebRTC**: 실시간 영상/음성 통신
- **Hilt**: 의존성 주입

## 의존성 규칙

- `data` 모듈은 `domain` 모듈에 의존하여 `Repository` 인터페이스와 `domain` 모델을 가져옵니다.
- 외부 라이_build.log
- `data` 모듈은 `domain` 모듈에 의존하여 `Repository` 인터페이스와 `domain` 모델을 가져옵니다.
- 외부 라이브러리(Retrofit, Room 등)에 대한 의존성을 가집니다.
- 다른 레이어(Presentation 등)에 직접적인 의존성을 가지지 않습니다.
