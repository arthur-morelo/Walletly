package com.proint.walletly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacao", schema = "geral")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Audited
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.hibernate.envers.NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_extrato_history")
    private ExtratoHistory extratoHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_conta", nullable = false)
    @NotNull(message = "A conta é obrigatória")
    private Conta conta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_categoria")
    private Categoria categoria;

    @Column(name = "descricao", nullable = false, length = 255)
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição não pode ter mais de 255 caracteres")
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @Column(name = "tipo_transacao", nullable = false, length = 10)
    @NotBlank(message = "O tipo de transação é obrigatório")
    @Size(max = 10, message = "O tipo de transação não pode ter mais de 10 caracteres")
    private String tipoTransacao;

    @Column(name = "data_transacao", nullable = false)
    @NotNull(message = "A data da transação é obrigatória")
    private LocalDate dataTransacao;
}