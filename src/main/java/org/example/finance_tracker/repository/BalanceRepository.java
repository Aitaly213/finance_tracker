package org.example.finance_tracker.repository;

import org.example.finance_tracker.entity.BanksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<BanksEntity,Long> {
    List<BanksEntity> findAllByUserId(Long userId);

    Optional<BanksEntity> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
