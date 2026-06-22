package com.proint.walletly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "extrato_history", schema = "geral")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExtratoHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private User usuario;

    @Column(name = "filename", nullable = false, length = 255)
    @NotBlank(message = "O nome do arquivo é obrigatório")
    @Size(max = 255, message = "O nome do arquivo não pode ter mais de 255 caracteres")
    private String filename;

    @Column(name = "upload_date", nullable = false)
    @NotNull(message = "A data de upload é obrigatória")
    private OffsetDateTime uploadDate;

    @Column(name = "status", nullable = false, length = 20)
    @NotBlank(message = "O status é obrigatório")
    private String status; // PENDENTE, PROCESSADO, ERRO

    @Column(name = "log_message", columnDefinition = "TEXT")
    private String logMessage;
}
