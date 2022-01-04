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

import java.util.List;
import java.util.Optional;
@Service
public class TransactionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class.getCanonicalName());
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getById(Long id){

        LOGGER.info("Fetching all the transactions using wallet id");

        List<Transaction> res = transactionRepository.findAllByWalletId(id);
        if(!res.isEmpty())return res;
        else
            throw new WalletException("Transaction with "+id+" does not exists!");
    }

    public boolean transfer(Long srcId, Long destId, Integer amount){
        if(amount<=0) throw new WalletException("Please enter a positive value");
        LOGGER.info("Transferring funds");
        Optional<Wallet> srcwallet = walletRepository.findById(srcId);
        Optional<Wallet> destwallet = walletRepository.findById(destId);

        if(srcwallet.isPresent() && destwallet.isPresent()){
            if(srcwallet.get().getCurrentBalance()>amount){
                srcwallet.get().setCurrentBalance(srcwallet.get().getCurrentBalance() - amount);
                destwallet.get().setCurrentBalance(destwallet.get().getCurrentBalance() + amount);
            }
            else{
                throw new WalletException("Insufficient Funds");
            }
            walletRepository.save(srcwallet.get());
            walletRepository.save(destwallet.get());
            Transaction t = new Transaction();
            t.setAmount(amount);
            t.setTransactionDate(new java.sql.Date(System.currentTimeMillis()));
            t.setWalletId(srcId);
            t.setDescription("Fund transfer from Wallet "+ srcId +" to Wallet " + destId);
            transactionRepository.save(t);

            return true;
        }
        throw new WalletException("Transaction is not possible");
    }



}
