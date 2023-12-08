package com.specificgroup.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyEvent {
    private String destinationEmail;
    private MessageType messageType;
    private String username;
}
