package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    public Account registerAccount(Account account) {
        // Check if username is blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }
        
        // Check if password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        
        // Check if account with username already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        // Save and return the new account
        return accountRepository.save(account);
    }
    
    public Account login(Account account) {
        if (account.getUsername() == null || account.getPassword() == null) {
            return null;
        }
        
        Optional<Account> existingAccount = accountRepository.findByUsernameAndPassword(
            account.getUsername(), account.getPassword());
        
        return existingAccount.orElse(null);
    }
    
    public boolean accountExistsById(Integer accountId) {
        return accountRepository.existsById(accountId);
    }
}
