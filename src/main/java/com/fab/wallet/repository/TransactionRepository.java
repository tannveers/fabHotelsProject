package com.fab.wallet.repository;

import com.fab.wallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
    List<Transaction> findAllByWalletId(Long walletId);
}
