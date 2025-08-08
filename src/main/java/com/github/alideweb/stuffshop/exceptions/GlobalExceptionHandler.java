package com.github.alideweb.stuffshop.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final ExceptionMapper mapper;

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseHttpException ex, HttpServletRequest request) {
        log.error("BaseHttpException caught: {} at {}", ex.getMessage(), request.getRequestURI(), ex);

        ProblemDetail pd = mapper.toProblemDetail(ex, request);
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleJsonParseError(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Invalid JSON at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Malformed JSON request");
        pd.setDetail("Could not parse JSON body");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_INVALID_JSON");

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingRequestParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error("Missing request parameter at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        String detail = String.format("Required query parameter '%s' is missing", ex.getParameterName());

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Missing request parameter");
        pd.setDetail(detail);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_MISSING_PARAMETER");

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ProblemDetail> handleMultipartError(MultipartException ex, HttpServletRequest request) {
        log.error("Multipart error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("File upload error");
        pd.setDetail("An error occurred while processing the file upload");
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_FILE_UPLOAD");

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolations(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("Constraint violation at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Constraint Violation");
        pd.setDetail(message);
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_CONSTRAINT_VIOLATION");

        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ProblemDetail pd = mapper.toInternalServerError(ex, request);
        return ResponseEntity.internalServerError().body(pd);
    }
}
