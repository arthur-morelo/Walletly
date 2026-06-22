package com.proint.walletly.repository;

import com.proint.walletly.model.ExtratoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtratoHistoryRepository extends JpaRepository<ExtratoHistory, Long> {
    List<ExtratoHistory> findByUsuarioIdOrderByUploadDateDesc(Long usuarioId);
}
