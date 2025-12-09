package com.kolloseum.fourpillars.common.exception;

import com.kolloseum.fourpillars.common.logger.ErrorLogger;
import com.kolloseum.fourpillars.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorLogger errorLogger;

    private ResponseEntity<ApiResponse<?>> handleErrorException(String code, String message) {
        ApiResponse<?> response = ApiResponse.error(code);
        errorLogger.logError(code, response.getTime(), message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<ApiResponse<?>> handleErrorException(ErrorCode errorCode, String message) {
        return handleErrorException(errorCode.getCode(), message);
    }

    // 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        return handleErrorException(ex.getErrorCode(), ex.getMessage());
    }

    // JWT 예외 처리
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException ex) {
        return handleErrorException(ex.getErrorCode(), ex.getMessage());
    }

    // OAuth2 관련 예외
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleOAuth2Exception(OAuth2AuthenticationException ex) {
        return handleErrorException(ErrorCode.O001, ex.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse<?>> handleRestClientException(RestClientException ex) {
        return handleErrorException(ErrorCode.O002, ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleDataAccessException(DataAccessException ex) {
        return handleErrorException(ErrorCode.D001, "Database operation failed");
    }

    // 기본 예외 처리 - 요청 파라미터 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return handleErrorException(ErrorCode.S001, message);
    }

    // 기본 예외 처리 - 잘못된 인수 전달
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return handleErrorException(ErrorCode.S002, ex.getMessage());
    }

    // 기본 예외 처리 - 런타임 예외
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        return handleErrorException(ErrorCode.S999, ex.getMessage());
    }

    // 정적 리소스 없음 예외 처리 (favicon.ico 등)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResourceFoundException(NoResourceFoundException ex) {
        // 로그를 남기지 않고 404 반환
        return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("S404"));
    }

    // 기본 예외 처리 - 모든 예외의 최종 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred", ex); // Log stack trace
        return handleErrorException(ErrorCode.S999, "Unexpected error: " + ex.getMessage());
    }
}