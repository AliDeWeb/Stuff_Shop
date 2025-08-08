package com.github.alideweb.stuffshop.common.dto;

import lombok.*;

@Builder
@Data
public class ApiResponse<T> {
    private String message;
    private T data;
    private int status;
}
