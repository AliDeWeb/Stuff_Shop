package com.github.alideweb.stuffshop.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionMapper mapper;

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseHttpException ex, HttpServletRequest request) {
        ProblemDetail pd = mapper.toProblemDetail(ex, request);
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Validation Failed");
        pd.setDetail(message);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_VALIDATION_FAILED");

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = mapper.toInternalServerError(ex, request);
        return ResponseEntity.internalServerError().body(pd);
    }
}
