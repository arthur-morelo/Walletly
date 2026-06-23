package com.proint.walletly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proint.walletly.model.Orcamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.proint.walletly.model.User;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    Page<Orcamento> findByUser(User user, Pageable pageable);
}
