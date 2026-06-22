package com.proint.walletly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Entity
@Table(name = "meta", schema = "geral")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Audited
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_usuario", nullable = false)
    @NotNull(message = "O usuário é obrigatório")
    private User user;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "O nome da meta é obrigatório")
    private String name;

    @Column(name = "goal_value", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "O valor da meta é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor da meta deve ser maior que zero")
    private BigDecimal goalValue;

    @Column(name = "saved_value", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal savedValue = BigDecimal.ZERO;
}
