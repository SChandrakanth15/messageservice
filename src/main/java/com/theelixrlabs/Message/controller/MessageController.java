package com.theelixrlabs.Message.controller;

import com.theelixrlabs.Message.constants.ApiPathsConstant;
import com.theelixrlabs.Message.dto.MessageRequest;
import com.theelixrlabs.Message.dto.MessageResponseDTO;
import com.theelixrlabs.Message.model.Message;
import com.theelixrlabs.Message.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPathsConstant.MESSAGES_BASE)
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @PostMapping(ApiPathsConstant.SEND_MESSAGE)
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        logger.info("Received request to send message from {} to {}",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                messageRequest.getReceiverUsername());
        try {
            Message sentMessage = messageService.sendMessage(
                    messageRequest.getReceiverUsername(),
                    messageRequest.getMessage()
            );
            logger.info("Message sent successfully to {}", messageRequest.getReceiverUsername());
            return ResponseEntity.ok(sentMessage);
        } catch (RuntimeException e) {
            logger.error("Error sending message to {}: {}", messageRequest.getReceiverUsername(), e.getMessage(), e);
            return ResponseEntity.status(500).body(null); // Error response
        }
    }

    @GetMapping(ApiPathsConstant.GET_USER_MESSAGES)
    public ResponseEntity<List<MessageResponseDTO>> getUserMessages() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Fetching latest messages for user: {}", username);
        try {
            logger.debug("Calling messageService to get the latest messages...");
            List<MessageResponseDTO> userMessages = messageService.getLatest10MessagesForUser(username);
            logger.info("Successfully fetched messages for user: {}", username);
            return ResponseEntity.ok(userMessages);
        } catch (RuntimeException e) {
            logger.error("Error fetching messages for user {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(500).build(); // Error response
        }
    }

    @GetMapping(ApiPathsConstant.GET_CHAT_HISTORY)
    public ResponseEntity<List<MessageResponseDTO>> getChatHistory(@PathVariable String selectedUsername) {
        logger.info("Fetching chat history for selected user: {}", selectedUsername);
        try {
            logger.debug("Calling messageService to get chat history...");
            List<MessageResponseDTO> chatHistory = messageService.getChatHistory(selectedUsername);
            logger.info("Successfully fetched chat history for {}", selectedUsername);
            return ResponseEntity.ok(chatHistory);
        } catch (RuntimeException e) {
            logger.error("Error fetching chat history for {}: {}", selectedUsername, e.getMessage(), e);
            return ResponseEntity.status(500).build(); // Error response
        }
    }
}
