package com.sun.caresyncsystem.exception;

import com.sun.caresyncsystem.dto.response.BaseApiResponse;
import com.sun.caresyncsystem.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;
    private static final Map<String, BaseErrorCode> errorCodeMap = Stream.of(
                    Stream.of(ErrorCode.values()),
                    Stream.of(ValidationError.values())
            ).flatMap(s -> s)
            .collect(Collectors.toMap(
                    Enum::name,
                    Function.identity()
            ));

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getCode())
                .body(buildErrorResponse(ErrorCode.ACCESS_DENIED));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleAppException(AppException exception) {
        BaseErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.badRequest()
                .body(buildErrorResponse(errorCode, exception.getArgs()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String enumKey = (fieldError != null) ? fieldError.getDefaultMessage() : "UNCATEGORIZED";
        BaseErrorCode errorCode = errorCodeMap.getOrDefault(enumKey, ErrorCode.INVALID_ERROR_KEY);

        return ResponseEntity.badRequest()
                .body(buildErrorResponse(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseApiResponse<Void>> handleUnexpectedException(Exception e) {
        return ResponseEntity.badRequest()
                .body(buildErrorResponse(ErrorCode.UNCATEGORIZED));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getCode())
                .body(buildErrorResponse(ErrorCode.UNCATEGORIZED));
    }

    private BaseApiResponse<Void> buildErrorResponse(BaseErrorCode errorCode, Object... args) {
        BaseApiResponse<Void> response = new BaseApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(messageUtil.getMessage(errorCode.getMessageKey(), args));
        return response;
    }
}
