package com.kolloseum.fourpillars.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    private BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public static BusinessException invalidBirthDate(String message) {
        return new BusinessException(ErrorCode.B001, message);
    }

    // 약관 관련
    public static BusinessException alreadyAgreedTerms(String message) {
        return new BusinessException(ErrorCode.B002, message);
    }

    public static BusinessException invalidTermsVersion(String message) {
        return new BusinessException(ErrorCode.B003, message);
    }

    // 사용자 관련
    public static BusinessException invalidUser(String message) {
        return new BusinessException(ErrorCode.B004, message);
    }

    public static BusinessException profileAlreadyCompleted(String message) {
        return new BusinessException(ErrorCode.B005, message);
    }

    public static BusinessException profileUpdateFailed(String message) {
        return new BusinessException(ErrorCode.B006, message);
    }

    public static BusinessException withdrawalFailed(String message) {
        return new BusinessException(ErrorCode.B007, message);
    }

    public static BusinessException termsAgreementRequired(String message) {
        return new BusinessException(ErrorCode.B008, message);
    }

    public static BusinessException profileIncompleted(String message) {
        return new BusinessException(ErrorCode.B009, message);
    }

    public static BusinessException birthTransformFailed(String message) {
        return new BusinessException(ErrorCode.B010, message);
    }

    // 파일/Excel 관련
    public static BusinessException fileNotFound(String message) {
        return new BusinessException(ErrorCode.F001, message);
    }

    public static BusinessException fileReadError(String message) {
        return new BusinessException(ErrorCode.F002, message);
    }

    public static BusinessException excelProcessError(String message) {
        return new BusinessException(ErrorCode.F003, message);
    }

    // Authorization 관련
    public static BusinessException accessDenied(String message) {
        return new BusinessException(ErrorCode.A001, message);
    }

    // Common
    public static BusinessException invalidRequest(String message) {
        return new BusinessException(ErrorCode.S002, message);
    }
}