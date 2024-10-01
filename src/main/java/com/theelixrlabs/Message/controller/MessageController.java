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
    public Message sendMessage(@RequestBody MessageRequest messageRequest) {
        logger.info("Received request to send message to {}: {}", messageRequest.getReceiverUsername(), messageRequest.getMessage());
        return messageService.sendMessage(
                messageRequest.getReceiverUsername(),
                messageRequest.getMessage()
        );
    }

//    @GetMapping("/inbox")
//    public List<Message> getMessages(@RequestParam String username) {
//        logger.info("Fetching inbox messages for user: {}", username);
//        return messageService.getUserMessages(username);
//    }

    @GetMapping(ApiPathsConstant.GET_USER_MESSAGES)
    public ResponseEntity<List<MessageResponseDTO>> getUserMessages() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("Fetching latest messages for user: {}", username);
        List<MessageResponseDTO> userMessages = messageService.getLatest10MessagesForUser(username);
        return ResponseEntity.ok(userMessages);
    }

    @GetMapping(ApiPathsConstant.GET_CHAT_HISTORY)
    public ResponseEntity<List<MessageResponseDTO>> getChatHistory(@PathVariable String selectedUsername) {
        logger.info("Fetching chat history for selected user: {}", selectedUsername);
        List<MessageResponseDTO> chatHistory = messageService.getChatHistory(selectedUsername);
        return ResponseEntity.ok(chatHistory);
    }
}
