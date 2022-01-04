package com.fab.wallet.controller;

import com.fab.wallet.entity.Wallet;
import com.fab.wallet.service.ValidationErrorService;
import com.fab.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/wallet")
@CrossOrigin
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private ValidationErrorService validationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return new ResponseEntity<>(walletService.getById(id),HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Wallet wallet, BindingResult result){
        ResponseEntity errors = validationService.validate(result);
        if(errors != null) return errors;
        walletService.updateProfile(wallet,id);
        return new ResponseEntity<>(walletService.getById(id),HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createorUpdate(@Valid @RequestBody Wallet wallet, BindingResult result){
        ResponseEntity errors = validationService.validate(result);
        if(errors != null) return errors;
        Wallet walletSaved = walletService.createOrUpdate(wallet);
        return new ResponseEntity<Wallet>(walletSaved,HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> addToWallet(@PathVariable Long id, @RequestParam int amount){
        walletService.addMoney(id,amount);
        return new ResponseEntity(HttpStatus.OK);
    }

}
