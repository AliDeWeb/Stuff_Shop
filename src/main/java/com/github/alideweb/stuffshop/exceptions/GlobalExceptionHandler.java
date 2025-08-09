package com.github.alideweb.stuffshop.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionMapper mapper;

    private static String newErrorId() {
        return UUID.randomUUID().toString();
    }

    private static void putErrorIdToMdc(String errorId) {
        MDC.put("errorId", errorId);
    }

    private static void clearErrorIdFromMdc() {
        MDC.remove("errorId");
    }

    private static void enrich(ProblemDetail pd, HttpServletRequest req, String errorId) {
        if (pd.getInstance() == null) {
            pd.setInstance(URI.create(req.getRequestURI()));
        }
        pd.setProperty("errorId", errorId);
        pd.setProperty("traceId", MDC.get("traceId"));
        pd.setProperty("spanId", MDC.get("spanId"));
        pd.setProperty("correlationId", MDC.get("correlationId"));
    }

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseHttpException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            log.error("BaseHttpException errorId={} path={} status={} code={}",
                    errorId, request.getRequestURI(), ex.getStatus(), ex.getStatus(), ex);

            ProblemDetail pd = mapper.toProblemDetail(ex, request);
            enrich(pd, request, errorId);
            return ResponseEntity.status(ex.getStatus()).body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            String message = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            log.error("ValidationFailed errorId={} path={} fields={}", errorId, request.getRequestURI(), message);

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("Validation Failed");
            pd.setDetail(message);
            pd.setInstance(URI.create(request.getRequestURI()));
            pd.setProperty("code", "ERR_VALIDATION_FAILED");

            enrich(pd, request, errorId);
            return ResponseEntity.badRequest().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleJsonParseError(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            log.error("InvalidJSON errorId={} path={}", errorId, request.getRequestURI(), ex);

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("Malformed JSON request");
            pd.setDetail("Could not parse JSON body");
            pd.setInstance(URI.create(request.getRequestURI()));
            pd.setProperty("code", "ERR_INVALID_JSON");

            enrich(pd, request, errorId);
            return ResponseEntity.badRequest().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingRequestParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            String detail = String.format("Required query parameter '%s' is missing", ex.getParameterName());
            log.error("MissingParam errorId={} path={} param={}", errorId, request.getRequestURI(), ex.getParameterName());

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("Missing request parameter");
            pd.setDetail(detail);
            pd.setInstance(URI.create(request.getRequestURI()));
            pd.setProperty("code", "ERR_MISSING_PARAMETER");

            enrich(pd, request, errorId);
            return ResponseEntity.badRequest().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ProblemDetail> handleMultipartError(MultipartException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            log.error("Multipart errorId={} path={}", errorId, request.getRequestURI(), ex);

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("File upload error");
            pd.setDetail("An error occurred while processing the file upload");
            pd.setInstance(URI.create(request.getRequestURI()));
            pd.setProperty("code", "ERR_FILE_UPLOAD");

            enrich(pd, request, errorId);
            return ResponseEntity.badRequest().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolations(ConstraintViolationException ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            String message = ex.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            log.error("ConstraintViolation errorId={} path={} violations={}", errorId, request.getRequestURI(), message);

            ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            pd.setTitle("Constraint Violation");
            pd.setDetail(message);
            pd.setInstance(URI.create(request.getRequestURI()));
            pd.setProperty("code", "ERR_CONSTRAINT_VIOLATION");

            enrich(pd, request, errorId);
            return ResponseEntity.badRequest().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, HttpServletRequest request) {
        String errorId = newErrorId();
        putErrorIdToMdc(errorId);
        try {
            log.error("Unexpected errorId={} path={}", errorId, request.getRequestURI(), ex);

            ProblemDetail pd = mapper.toInternalServerError(ex, request);
            enrich(pd, request, errorId);
            return ResponseEntity.internalServerError().body(pd);
        } finally {
            clearErrorIdFromMdc();
        }
    }
}
