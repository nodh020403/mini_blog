package io.donghun.blog.common.eventhandler;

import io.donghun.blog.common.constants.ErrorCode;
import io.donghun.blog.common.dto.ApiResponse;
import io.donghun.blog.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        ErrorCode code = exception.getErrorCode();
        return ResponseEntity
                .status(
                        code.getStatus()
                ).body(
                        ApiResponse.fail(
                                code.name(),
                                exception.getMessage())
                );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        return ResponseEntity.
                badRequest().
                body(
                        ApiResponse.fail(
                                ErrorCode.VALIDATION_FAILED.name(),
                                ErrorCode.VALIDATION_FAILED.getDescription())
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(
                        ErrorCode.INTERNAL_ERROR.name(),
                        ErrorCode.INTERNAL_ERROR.getDescription()
                ));
    }
}
