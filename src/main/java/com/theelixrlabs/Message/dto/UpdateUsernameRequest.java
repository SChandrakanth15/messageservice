package com.theelixrlabs.Message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUsernameRequest {
    private String oldUsername;
    private String newUsername;

}

