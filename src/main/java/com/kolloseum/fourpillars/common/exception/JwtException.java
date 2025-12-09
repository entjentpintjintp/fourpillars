package com.kolloseum.fourpillars.common.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {
    private final ErrorCode errorCode;

    private JwtException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { 
        return errorCode.getCode(); 
    }

    // JWT 관련 예외 팩토리 메서드
    public static JwtException invalidToken(String message) {
        return new JwtException(ErrorCode.J001, message);
    }

    public static JwtException expiredToken(String message) {
        return new JwtException(ErrorCode.J002, message);
    }

    public static JwtException blacklistedToken(String message) {
        return new JwtException(ErrorCode.J003, message);
    }

    public static JwtException invalidTokenState(String message) {
        return new JwtException(ErrorCode.J004, message);
    }

    public static JwtException generationError(String message) {
        return new JwtException(ErrorCode.J005, message);
    }

    public static JwtException parsingError(String message) {
        return new JwtException(ErrorCode.J006, message);
    }
}