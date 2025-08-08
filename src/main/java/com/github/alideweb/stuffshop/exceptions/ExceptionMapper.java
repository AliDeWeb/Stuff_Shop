package com.github.alideweb.stuffshop.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ExceptionMapper {
    public ProblemDetail toProblemDetail(BaseHttpException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(ex.getStatus());

        pd.setTitle("Error: " + ex.getErrorCode());
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", ex.getErrorCode());

        return pd;
    }

    public ProblemDetail toInternalServerError(Exception ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        pd.setTitle("Unexpected error");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("code", "ERR_INTERNAL");

        return pd;
    }
}
