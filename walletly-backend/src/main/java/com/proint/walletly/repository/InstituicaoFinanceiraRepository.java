package com.proint.walletly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proint.walletly.model.InstituicaoFinanceira;

@Repository
public interface InstituicaoFinanceiraRepository extends JpaRepository<InstituicaoFinanceira, Long> {
    java.util.Optional<InstituicaoFinanceira> findByNome(String nome);
}
