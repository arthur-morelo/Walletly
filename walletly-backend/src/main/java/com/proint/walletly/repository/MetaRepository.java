package com.proint.walletly.repository;

import com.proint.walletly.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.proint.walletly.model.User;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByUserId(Long userId);
    Page<Meta> findByUser(User user, Pageable pageable);
}
