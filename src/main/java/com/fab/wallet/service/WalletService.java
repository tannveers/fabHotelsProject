package com.fab.wallet.service;

import com.fab.wallet.entity.Transaction;
import com.fab.wallet.entity.Wallet;
import com.fab.wallet.exception.WalletException;
import com.fab.wallet.repository.TransactionRepository;
import com.fab.wallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class.getCanonicalName());

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    public Wallet getById(Long id) {

        LOGGER.info("Fetching Wallet details by ID");

        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        throw new WalletException("Wallet with " + id + " does not exists!");
    }

    public Wallet createOrUpdate(Wallet wallet) {
        LOGGER.info("Creating wallet if it does not exist");
        walletRepository.save(wallet);
        return wallet;
    }

    public boolean addMoney(Long id, Integer money) {
        LOGGER.info("Adding Money to wallet");
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            wallet.get().setCurrentBalance(wallet.get().getCurrentBalance() + money);
            walletRepository.save(wallet.get());
            Transaction t = new Transaction();
            t.setAmount(money);
            t.setTransactionDate(new java.sql.Date(System.currentTimeMillis()));
            t.setWalletId(id);
            t.setDescription("Adding money to wallet "+id);
            transactionRepository.save(t);
            return true;
        }
        throw new WalletException("Wallet with " + id + " does not exists!");

    }

    public void updateProfile(Wallet wallet, Long id) {
        LOGGER.info("Updating wallet");
        Optional<Wallet> reswallet = walletRepository.findById(id);
        if (reswallet.isPresent()) {
           reswallet.get().setName(wallet.getName());
           reswallet.get().setDescription(wallet.getDescription());
           walletRepository.save(reswallet.get());
        }
        else throw new WalletException("Wallet with " + id + " does not exists!");

    }
}
