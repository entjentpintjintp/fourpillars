package com.kolloseum.fourpillars.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // B001~B099: Business 관련 (프로필, 약관 등)
    B001("B001", "Invalid birth date format"),
    B002("B002", "Terms already agreed"),
    B003("B003", "Invalid terms version"),
    B004("B004", "User not found"),
    B005("B005", "Profile already completed"),
    B006("B006", "Profile update failed"),
    B007("B007", "Withdrawal failed"),
    B008("B008", "Terms agreement required"),
    B009("B009", "Profile incompleted"),
    B010("B010", "Birth transform failed"),

    // F001~F099: File/Excel 관련
    F001("F001", "File not found"),
    F002("F002", "File read error"),
    F003("F003", "Excel processing error"),

    // J001~J099: JWT 관련
    J001("J001", "Invalid JWT token"),
    J002("J002", "Expired JWT token"),
    J003("J003", "Blacklisted JWT token"),
    J004("J004", "Invalid token state"),
    J005("J005", "JWT token generation failed"),
    J006("J006", "JWT token parsing failed"),

    // O001~O099: OAuth2/외부 서비스 관련
    O001("O001", "OAuth2 authentication failed"),
    O002("O002", "External service error"),

    // D001~D099: Database 관련
    D001("D001", "Database operation failed"),

    // A001~A099: Authorization 관련
    A001("A001", "Access denied"),

    // S001~S999: System/공통 오류
    S001("S001", "Validation failed"),
    S002("S002", "Bad request"),
    S003("S003", "Resource not found"),
    S999("S999", "Internal server error");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}