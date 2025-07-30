package com.github.alideweb.stuffshop.modules.products.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateRequestDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.01")
    private BigDecimal price;
}
