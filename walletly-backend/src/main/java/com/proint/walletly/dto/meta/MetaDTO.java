package com.proint.walletly.dto.meta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MetaDTO(
        Long id,
        @NotNull(message = "O usuário é obrigatório") Long usuarioId,
        @NotBlank(message = "O nome da meta é obrigatório") String nome,
        @NotNull(message = "O valor da meta é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valorMeta,
        @NotNull(message = "O valor atual é obrigatório")
        BigDecimal valorAtual
) {
}
