package com.proint.walletly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.proint.walletly.model.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "conta", schema = "geral")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Audited
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_instituicao", nullable = false)
    @NotNull(message = "A instituição financeira é obrigatória")
    private com.proint.walletly.model.InstituicaoFinanceira instituicao;

    @Column(name = "apelido", nullable = false, length = 100)
    @NotBlank(message = "O apelido é obrigatório")
    @Size(max = 100, message = "O apelido não pode ter mais de 100 caracteres")
    private String apelido;

    @Column(name = "tipo_conta", nullable = false, length = 50)
    @NotBlank(message = "O tipo de conta é obrigatório")
    @Size(max = 50, message = "O tipo de conta não pode ter mais de 50 caracteres")
    private String tipoConta;

    @Column(name = "saldo_atual", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "O saldo atual é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a 0")
    private BigDecimal saldoAtual;

    @Column(name = "data_ultima_sincronizacao")
    private OffsetDateTime dataUltimaSincronizacao;
}