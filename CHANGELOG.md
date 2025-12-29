# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.1] - 2025-12-26
### Fixed
- **Deployment Script (`start.sh`):**
    - Added automatic process termination (kill) before starting new instance.
    - Added dynamic JAR file detection to support version increments.
    - Added `-Dspring.profiles.active=prod` to enforce production profile.
- **Database:**
    - Fixed schema validation issues by enforcing `validate` (via prod profile).

## [1.0.0] - 2025-12-26
### Added
- **Initial Release:**
    - Core features: Monthly Fortune, User Profile, OAuth2 Login (Google/Kakao).
    - Admin Portal: Google Login, TOTP (Two-Factor Auth), Spring Boot Admin Monitoring.
    - Infrastructure: Docker Compose (MariaDB, Redis, Nginx, Certbot).
    - Domain: `kolloseum.com` with HTTPS support.
