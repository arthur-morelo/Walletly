package com.proint.walletly.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "curso", schema = "geral")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Audited
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "O título é obrigatório")
    private String title;

    @Column(name = "text_left")
    private String textLeft;

    @Column(name = "text_right")
    private String textRight;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;
}
