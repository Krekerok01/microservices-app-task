package com.specificgroup.user.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageDto {
    private String destinationEmail;
    private MessageType messageType;
    private MessageContent content;
}