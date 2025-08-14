


/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SocialMediaController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessageService messageService;
    
    // User Story 1: Register new user
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.registerAccount(account);
            if (registeredAccount == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok(registeredAccount);
        } catch (RuntimeException e) {
            // Username already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    // User Story 2: User login
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.login(account);
        if (loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(loggedInAccount);
    }
    
    // User Story 3: Create new message
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(createdMessage);
    }
    
    // User Story 4: Retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
    
    // User Story 5: Retrieve message by ID
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.ok().build(); // Empty response body with 200 status
        }
        return ResponseEntity.ok(message);
    }
    
    // User Story 6: Delete message by ID
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        Integer rowsUpdated = messageService.deleteMessage(messageId);
        if (rowsUpdated == 1) {
            return ResponseEntity.ok(rowsUpdated);
        }
        return ResponseEntity.ok().build(); // Empty response body with 200 status
    }
    
    // User Story 7: Update message by ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, 
                                               @RequestBody Map<String, String> requestBody) {
        String newMessageText = requestBody.get("messageText");
        Integer rowsUpdated = messageService.updateMessage(messageId, newMessageText);
        if (rowsUpdated == 1) {
            return ResponseEntity.ok(rowsUpdated);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
    // User Story 8: Get all messages by user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }
}