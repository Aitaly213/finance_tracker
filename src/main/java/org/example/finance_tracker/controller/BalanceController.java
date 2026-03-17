package org.example.finance_tracker.controller;

import org.example.finance_tracker.dto.BanksDto;
import org.example.finance_tracker.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping()
    public ResponseEntity<Integer> getTotalBalance() {
        return ResponseEntity.ok().body(balanceService.getTotalBalance());
    }

    @GetMapping("/banks")
    public ResponseEntity<List<BanksDto>> getAllBanks() {
        return ResponseEntity.ok().body(balanceService.getAllBanks());
    }

    @GetMapping("/banks/{id}")
    public ResponseEntity<BanksDto> getBankById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(balanceService.getBankById(id));
    }

    @PostMapping("/banks")
    public ResponseEntity<BanksDto> addBankBalance(
            @RequestBody BanksDto bankToCreate
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(balanceService.addBalance(bankToCreate));
    }


    @DeleteMapping("/banks/{id}")
    public ResponseEntity<Void> deleteBank(
            @PathVariable Long id
    ){
        balanceService.deleteBank(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/banks/{id}")
    public ResponseEntity<BanksDto> updateBank(
            @PathVariable Long id,
            @RequestBody BanksDto bankToUpdate
    ){
        return ResponseEntity.ok().body(balanceService.updateBank(id,bankToUpdate));
    }

}
