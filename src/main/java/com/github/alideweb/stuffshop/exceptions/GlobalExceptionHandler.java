package com.github.alideweb.stuffshop.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionMapper mapper;

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseHttpException ex, HttpServletRequest request) {
        ProblemDetail pd = mapper.toProblemDetail(ex, request);
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = mapper.toInternalServerError(ex, request);
        return ResponseEntity.internalServerError().body(pd);
    }
}
