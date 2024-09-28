package com.theelixrlabs.Message.service;

import com.theelixrlabs.Message.dto.MessageResponseDTO;
import com.theelixrlabs.Message.model.Message;
import com.theelixrlabs.Message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss");
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(String receiverUsername, String message) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Message messages = new Message();
        messages.setSenderUsername(currentUserName);
        messages.setReceiverUsername(receiverUsername);
        messages.setMessage(message);
        messages.setTimeStamp(LocalDateTime.now());
        return messageRepository.save(messages);
    }

    public List<Message> getUserMessages(String username) {
        return messageRepository.findBySenderUsernameOrReceiverUsername(username, username);
    }

    public List<MessageResponseDTO> getLatest10MessagesForUser(String username) {
        List<Message> messages = messageRepository.findBySenderUsernameOrReceiverUsername(username, username);

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

    // New method to get chat history
    public List<MessageResponseDTO> getChatHistory(String selectedUsername) {
        // Get the logged-in user's username
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        // Fetch messages exchanged between the logged-in user and the selected user
        List<Message> chatHistory = messageRepository.findBySenderUsernameAndReceiverUsernameOrSenderUsernameAndReceiverUsername(
                currentUserName, selectedUsername, selectedUsername, currentUserName);

        // Map messages to DTOs with formatted timestamps
        return chatHistory.stream()
                .map(this::mapToMessageResponseDTO)
                .collect(Collectors.toList());
    }
}
