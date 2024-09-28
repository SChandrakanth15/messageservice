package com.theelixrlabs.Message.dto;

import lombok.Data;

@Data
public class MessageResponseDTO {
    private String id;
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private String timeStamp; // Formatted timestamp
}

