package com.specificgroup.notification.dto.message;

import com.specificgroup.notification.dto.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageContent implements Content {
    private String header;
    private String text;
}
