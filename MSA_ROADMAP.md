# 아키텍처 진화 및 MSA 도입 로드맵 (Architecture Evolution Roadmap)

**프로젝트:** FourPillars  
**작성일:** 2025-12-30  

---

## 1. 개요 (Overview)
본 문서는 프로젝트 규모 확장(Scale-up) 시 고려해야 할 아키텍처의 단계적 진화 방향을 제시합니다.
현재의 **단일체(Monolithic)** 구조에서 **마이크로서비스(MSA)**로 전환하기 위한 판단 기준과 전략을 정의합니다.

---

## 2. 현재 아키텍처: 고효율 모듈형 모놀리스 (Phase 1)

**"작고, 빠르고, 단단하다."**

현재 `FourPillars`는 **모듈형 모놀리스(Modular Monolith)**에 가깝게 설계되어 있습니다.
(패키지가 `application`, `domain`, `interfaces`로 명확히 분리됨)

*   **구조:** 하나의 Spring Boot 애플리케이션 안에 모든 기능(Auth, Fortune, Admin)이 통합됨.
*   **장점:**
    1.  **배포 용이성:** JAR 파일 하나만 배포하면 끝.
    2.  **비용 절감:** 4GB 서버 1대로 DAU 1,000~5,000명까지 처리 가능 (가장 경제적).
    3.  **트랜잭션 관리:** DB가 하나라 `Since Transaction` 관리가 쉬움.
*   **단점:** 트래픽이 한 기능(예: 운세 조회)에만 몰려도 전체 서버가 느려질 수 있음.

---

## 3. 과도기: 로드 밸런싱 및 조회 전용 서버 분리 (Phase 2)

**"쌍둥이 서버를 만든다." (Scale-Out)**

DB 병목보다 웹 서버 병목이 먼저 올 경우(예: DAU 1만 명 돌파), MSA로 가기 전 **가장 현실적이고 효과적인** 단계입니다.

*   **변경점:**
    *   똑같은 서버를 2대 띄움 (Server A, Server B).
    *   앞단에 `Nginx`를 L7 로드밸런서로 사용하여 트래픽 분산.
    *   DB는 하나를 공유하되, **Master(쓰기)/Slave(읽기)**로 복제(Replication)하여 조회 성능 극대화.
*   **비용:** 약 2~3배 증가 (서버 2대 + DB 분리).

---

## 3.5. [확정] 최종 아키텍처: 인증/비즈니스 서버 분리 (Lite MSA)

**"데이팅 앱을 위한 최적의 이원화 전략"**

최종 승인권자(User)의 결정에 따라, 본 프로젝트는 **인증(Auth)**과 **매칭/비즈니스(Business)**를 물리적으로 분리하는 **Lite MSA** 구조를 채택합니다.
데이팅 앱 특성상 "로그인/프로필 조회" 트래픽과 "매칭/스와이프" 트래픽의 성격이 다르므로, 이를 분리하는 것은 매우 타당한 설계입니다.

### 3.5.1 아키텍처 구조 (Spring Gateway 미사용)
별도의 `Spring Cloud Gateway` 서버를 두지 않고, 기존 **Nginx**를 활용하여 시스템 복잡도를 낮춥니다.

1.  **Nginx (Reverse Proxy):** 트래픽 라우팅 담당
    *   `api.kolloseum.com/auth` -> **Auth Server (8081)**
    *   `api.kolloseum.com/api` -> **Business Server (8080)**
2.  **Auth Server (User & Identity):**
    *   회원가입, 로그인, JWT 발급, 프로필(User Profile) 관리.
    *   데이팅 앱의 핵심인 "유저 데이터"의 무결성 보장.
3.  **Business Server (Matching & Fortune):**
    *   사주 계산, 매칭 알고리즘, 좋아요/채팅 기능.
    *   Auth Server가 발급한 JWT를 **Stateless**하게 검증하여 성능 극대화.

### 3.5.2 채택 사유 (Decision Record)
*   ** 확장성:** 매칭 알고리즘이 무거워져도 로그인 서버에는 영향이 없음.
*   ** 유지보수:** 회원 로직이나 DB 스키마 변경 시 매칭 서버를 재배포할 필요 없음.
*   ** 효율성:** Gateway 서버를 따로 띄우는 오버헤드 제거 (Nginx 활용).

---

## 4. 최종 목표: 마이크로서비스 아키텍처 (Phase 3 - MSA)

**"각 방을 따로 쓴다." (Decomposition)**

시스템 복잡도가 극에 달하거나, 개발 팀이 10명 이상으로 늘어났을 때 도입합니다.

### 4.1. 서비스 분리 기준 (Domain-Driven Design)
도메인 주도 설계(DDD)에 따라 다음과 같이 3개의 마이크로서비스로 쪼갭니다.

1.  **인증 서비스 (Identity Service):**
    *   로그인, 회원가입, JWT 발급 전담.
    *   장애가 나도 기로그인 사용자는 서비스 이용 가능해야 함.
2.  **운세 서비스 (Fortune Service):**
    *   만세력 계산 및 운세 조회 전담.
    *   CPU 연산이 많으므로 고사양 CPU 서버 할당.
    *   자체 Redis 캐시를 보유하여 독립적 운영.
3.  **알림/결제 서비스 (Notification/Billing Service):**
    *   비동기 처리가 많으므로 Kafka/RabbitMQ 같은 메시지 큐 도입 필요.

### 4.2. 필수 도입 기술 (Infrastructure Complexity)
MSA를 하려면 단순 코딩 외에 "운영 인프라"가 필수입니다.
*   **Service Discovery:** 서로의 IP를 찾기 위해 `Netflix Eureka` 또는 `K8s DNS` 도입.
*   **API Gateway:** 모든 요청의 입구 (`Spring Cloud Gateway`).
*   **Circuit Breaker:** 한 서비스가 죽었을 때 전파를 막는 차단기 (`Resilience4j`).
*   **Distributed Tracing:** 로그를 추적하기 위한 `Zipkin` + `Sleuth`.

---

## 5. 결론 및 제언 (Conclusion)

최종 프로젝트의 규모를 고려할 때, **"무조건 MSA 도입"보다는 "MSA를 할 수 있도록 모듈을 잘 나눠놓는 것"**이 핵심입니다.

**[추천 전략]**
1.  **현재:** Phase 1 (모놀리스) 구조 유지. 배포와 운영 복잡도를 0으로 만듦.
2.  **문서화:** "나중에 이렇게 쪼개겠다"는 본 로드맵(`MSA_ROADMAP.md`)을 제출하여 **아키텍처 설계 능력**을 어필.
3.  **코드:** `Auth` 패키지와 `Fortune` 패키지 간의 의존성을 최소화하여, 언제든 찢어낼 수 있게 유지.

**"설계를 할 줄 알지만, 오버엔지니어링을 피하기 위해 지금은 모놀리스를 선택했다"**는 모범 답안이 됩니다.
