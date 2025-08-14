package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private AccountService accountService;
    
    public Message createMessage(Message message) {
        // Check if messageText is blank
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            return null;
        }
        
        // Check if messageText is over 255 characters
        if (message.getMessageText().length() > 255) {
            return null;
        }
        
        // Check if postedBy refers to a real, existing user
        if (message.getPostedBy() == null || !accountService.accountExistsById(message.getPostedBy())) {
            return null;
        }
        
        return messageRepository.save(message);
    }
    
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    
    public Message getMessageById(Integer messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.orElse(null);
    }
    
    public Integer deleteMessage(Integer messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }
    
    public Integer updateMessage(Integer messageId, String newMessageText) {
        // Check if messageText is blank or over 255 characters
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
            return 0;
        }
        
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
    }
    
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
