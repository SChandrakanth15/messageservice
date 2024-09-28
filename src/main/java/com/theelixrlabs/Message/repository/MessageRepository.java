package com.theelixrlabs.Message.repository;

import com.theelixrlabs.Message.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<Message, UUID> {
    List<Message> findBySenderUsernameOrReceiverUsername(String senderUsername, String receiverUsername);

    List<Message> findTop10BySenderUsernameOrderByTimeStampDesc(String senderUsername);

    // New method to find messages between two users
    List<Message> findBySenderUsernameAndReceiverUsernameOrSenderUsernameAndReceiverUsername(
            String senderUsername1, String receiverUsername1, String senderUsername2, String receiverUsername2);
}
