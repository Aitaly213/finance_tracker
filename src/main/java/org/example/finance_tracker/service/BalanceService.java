package org.example.finance_tracker.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.finance_tracker.Utils.mapper.Mapper;
import org.example.finance_tracker.dto.BanksDto;
import org.example.finance_tracker.entity.BanksEntity;
import org.example.finance_tracker.entity.UserEntity;
import org.example.finance_tracker.repository.BalanceRepository;
import org.example.finance_tracker.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BalanceService {

    private final BalanceRepository repository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public BalanceService(BalanceRepository repository, UserRepository userRepository,
                          Mapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    private UserEntity getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


    public int getTotalBalance() {

        UserEntity user = getCurrentUser();

        int total = 0;
        for (BanksEntity e : repository.findAllByUserId(user.getId())) {
            total += e.getBalance();
        }
        return total;
    }

    public BanksDto addBalance(BanksDto bankToCreate) {
        UserEntity user = getCurrentUser();

        var entity = new BanksEntity(null, bankToCreate.name(), bankToCreate.balance(), user);

        var savedEntity = repository.save(entity);

        return mapper.toDtoBank(savedEntity);
    }

    public List<BanksDto> getAllBanks() {
        UserEntity user = getCurrentUser();

        var banks = repository.findAllByUserId(user.getId());

        return banks.stream().map(mapper::toDtoBank).toList();
    }

    public BanksDto getBankById(Long id) {
        UserEntity user = getCurrentUser();


        BanksEntity bank = repository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new EntityNotFoundException("BAnk not found by id =" + id));

        return mapper.toDtoBank(bank);
    }

    @Transactional
    public void deleteBank(Long id) {
        UserEntity user = getCurrentUser();
        if (!repository.existsByIdAndUserId(id, user.getId())) {
            throw new EntityNotFoundException("Not found bank by this id : " + id);
        }

        repository.deleteByIdAndUserId(id, user.getId());

    }

    public BanksDto updateBank(Long id, BanksDto bankToUpdate) {
        UserEntity user = getCurrentUser();
        var oldBankEntity = repository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new EntityNotFoundException("Bank not found by id =" + id));


        var newBankEntity = new BanksEntity(
                oldBankEntity.getId(),
                bankToUpdate.name(),
                bankToUpdate.balance(),
                user
        );

        repository.save(newBankEntity);
        return mapper.toDtoBank(newBankEntity);
    }
}
