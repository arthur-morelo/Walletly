package com.proint.walletly.repository;

import com.proint.walletly.model.ExtratoHistory;
import com.proint.walletly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtratoHistoryRepository extends JpaRepository<ExtratoHistory, Long> {
    List<ExtratoHistory> findByUserOrderByUploadDateDesc(User user);
}
