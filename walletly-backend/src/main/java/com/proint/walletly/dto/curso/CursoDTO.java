package com.proint.walletly.dto.curso;

import jakarta.validation.constraints.NotBlank;

public record CursoDTO(
        Long id,
        @NotBlank(message = "O título é obrigatório")
        String title,
        String textLeft,
        String textRight,
        String courseDescription
) {
}
