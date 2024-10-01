package com.theelixrlabs.Message.service;

import com.theelixrlabs.Message.constants.MessageConstant;
import com.theelixrlabs.Message.dto.MessageResponseDTO;
import com.theelixrlabs.Message.model.Message;
import com.theelixrlabs.Message.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(MessageConstant.DATE_TIME_FORMATTER);

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(String receiverUsername, String message) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Message messages = new Message();
        messages.setSenderUsername(currentUserName);
        messages.setReceiverUsername(receiverUsername);
        messages.setMessage(message);
        messages.setTimeStamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(messages);
        logger.info("Message sent from {} to {}: {}", currentUserName, receiverUsername, message);
        return savedMessage;
    }

//    public List<Message> getUserMessages(String username) {
//        logger.info("Fetching messages for user: {}", username);
//        return messageRepository.findBySenderUsernameOrReceiverUsername(username, username);
//    }

    public List<MessageResponseDTO> getLatest10MessagesForUser(String username) {
        List<Message> messages = messageRepository.findBySenderUsernameOrReceiverUsername(username, username);

        logger.info("Fetching latest 10 messages for user: {}", username);
        return messages.stream()
                .sorted((m1, m2) -> m2.getTimeStamp().compareTo(m1.getTimeStamp())) // Sort by timestamp descending
                .limit(10) // Limit to the latest 10 messages
                .map(this::mapToMessageResponseDTO) // Map to DTO with formatted timestamp
                .collect(Collectors.toList());
    }

    private MessageResponseDTO mapToMessageResponseDTO(Message message) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(message.getId().toString());
        dto.setSenderUsername(message.getSenderUsername());
        dto.setReceiverUsername(message.getReceiverUsername());
        dto.setMessage(message.getMessage());
        dto.setTimeStamp(message.getTimeStamp().format(FORMATTER)); // Format the timestamp
        return dto;
    }

    public List<MessageResponseDTO> getChatHistory(String selectedUsername) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        logger.info("Fetching chat history between {} and {}", currentUserName, selectedUsername);
        List<Message> chatHistory = messageRepository.findBySenderUsernameAndReceiverUsernameOrSenderUsernameAndReceiverUsername(
                currentUserName, selectedUsername, selectedUsername, currentUserName);

        return chatHistory.stream()
                .map(this::mapToMessageResponseDTO)
                .collect(Collectors.toList());
    }
}
