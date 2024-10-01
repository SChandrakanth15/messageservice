package com.theelixrlabs.Message.model;

import com.theelixrlabs.Message.constants.MessageConstant;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = MessageConstant.MESSAGES_COLLECTION_NAME)
public class Message {
    private UUID id = UUID.randomUUID();
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private LocalDateTime timeStamp;
}
