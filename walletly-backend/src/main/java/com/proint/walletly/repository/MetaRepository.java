package com.proint.walletly.repository;

import com.proint.walletly.model.Meta;
import com.proint.walletly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByUser(User user);
}
