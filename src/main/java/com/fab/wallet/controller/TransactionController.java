package com.fab.wallet.controller;

import com.fab.wallet.entity.Transaction;
import com.fab.wallet.service.TransactionService;
import com.fab.wallet.service.ValidationErrorService;
import com.fab.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class.getCanonicalName());
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ValidationErrorService validationService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return new ResponseEntity<>(transactionService.getById(id),HttpStatus.OK);
    }

    @GetMapping("/transfer")
    public ResponseEntity<?> transferFunds(@RequestParam Long srcId,@RequestParam Long destId, Integer amount)
    {
        LOGGER.info("Fetching Wallet details by ID");
        transactionService.transfer(srcId,destId,amount);
        return new ResponseEntity<Transaction>(HttpStatus.OK);
    }
}
