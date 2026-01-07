# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] - 2026-01-07
### Added
- **Siju (Time Pillar) Implementation:**
    - **Siju Calculation:** Implemented "Sidu-beop" (Time Head Rule) to calculate the Siju (Time Pillar) based on Day Pillar (Gapja) and birth time.
    - **TimeBranch Support:** Added `TimeBranch` Enum (12 Earthly Branches) to handle birth time inputs (e.g., 'ja', 'chuk').
    - **User Profile:** Added `birthTime` field to User Profile.
    - **API Endpoint:** Added `GET /users/fortune` to retrieve User's Four Pillars (Year, Month, Day, Time) in Korean (e.g., "갑자", "병인").

## [1.1.0] - 2025-12-31
### Added
- **Notice System (공지사항):**
    - **Database:** `notices` table (id, title, content, dates).
    - **App API (`ContentController`):**
        - `GET /contents/notices`: Returns lightweight list (id, title, dates).
        - `GET /contents/notices/{id}`: Returns detailed content (HTML).
        - **Format:** `ApiResponse` with `code`, `time`, `data`.
    - **Admin API (`AdminNoticeController`):**
        - CRUD Endpoints (`POST`, `PUT`, `DELETE`).
- **Error Handling:**
    - Added `S003` (Resource not found) to `ErrorCode`.

## [1.0.1] - 2025-12-26
### Fixed
- **Deployment Script (`start.sh`):**
    - Added automatic process termination (kill) before starting new instance.
    - Added dynamic JAR file detection to support version increments.
    - Added `-Dspring.profiles.active=prod` to enforce production profile.
- **Database:**
    - Fixed schema validation issues by enforcing `validate` (via prod profile).
### Optimized
- **Performance Tuning (4GB Server):**
    - Increased Database Connection Pool (`HikariCP`) to 30 (from default 10).
    - Increased Web Server Threads (`Tomcat`) to 300 (from default 200).
    - Fixed JVM Heap Memory to 1.5GB (`-Xms1536m -Xmx1536m`) for stability.

## [1.0.0] - 2025-12-26
### Added
- **Initial Release:**
    - Core features: Monthly Fortune, User Profile, OAuth2 Login (Google/Kakao).
    - Admin Portal: Google Login, TOTP (Two-Factor Auth), Spring Boot Admin Monitoring.
    - Infrastructure: Docker Compose (MariaDB, Redis, Nginx, Certbot).
    - Domain: `kolloseum.com` with HTTPS support.
