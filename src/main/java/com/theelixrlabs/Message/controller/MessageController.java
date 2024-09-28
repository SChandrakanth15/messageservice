package com.theelixrlabs.Message.controller;

import com.theelixrlabs.Message.dto.MessageRequest;
import com.theelixrlabs.Message.dto.MessageResponseDTO;
import com.theelixrlabs.Message.dto.UpdateUsernameRequest;
import com.theelixrlabs.Message.model.Message;
import com.theelixrlabs.Message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody MessageRequest messageRequest) {
        return messageService.sendMessage(
                messageRequest.getReceiverUsername(),
                messageRequest.getMessage()
        );
    }

    @GetMapping("/inbox")
    public List<Message> getMessages(@RequestParam String username) {
        return messageService.getUserMessages(username);
    }

    @GetMapping("/user")
    public ResponseEntity<List<MessageResponseDTO>> getUserMessages() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MessageResponseDTO> userMessages = messageService.getLatest10MessagesForUser(username);
        return ResponseEntity.ok(userMessages);
    }

    // New endpoint to get chat history
    @GetMapping("/history/{selectedUsername}")
    public ResponseEntity<List<MessageResponseDTO>> getChatHistory(@PathVariable String selectedUsername) {
        List<MessageResponseDTO> chatHistory = messageService.getChatHistory(selectedUsername);
        return ResponseEntity.ok(chatHistory);
    }
}
