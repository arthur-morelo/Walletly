package com.proint.walletly.dto.meta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MetaDTO(
        Long id,
        @NotBlank(message = "O nome é obrigatório")
        String name,
        @NotNull(message = "O valor da meta é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor da meta deve ser maior que zero")
        BigDecimal goalValue,
        BigDecimal savedValue
) {
}
