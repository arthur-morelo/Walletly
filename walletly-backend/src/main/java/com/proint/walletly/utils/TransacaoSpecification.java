package com.proint.walletly.utils;

import com.proint.walletly.model.Transacao;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoSpecification {

    public static Specification<Transacao> withFilters(
            Long usuarioId,
            Long contaId,
            Long categoriaId,
            String tipoTransacao,
            String descricao,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (usuarioId != null) {
                predicates.add(criteriaBuilder.equal(root.join("conta").join("usuario").get("id"), usuarioId));
            }

            if (contaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("conta").get("id"), contaId));
            }

            if (categoriaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoria").get("id"), categoriaId));
            }

            if (tipoTransacao != null && !tipoTransacao.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("tipoTransacao"), tipoTransacao));
            }

            if (descricao != null && !descricao.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("descricao")),
                        "%" + descricao.toLowerCase() + "%"
                ));
            }

            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataTransacao"), dataInicio));
            }

            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataTransacao"), dataFim));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

