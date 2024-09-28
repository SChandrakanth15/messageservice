package com.theelixrlabs.Message.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String receiverUsername;
    private String message;
}
