package com.specificgroup.logs.kafka.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEvent {
    String level;
    String loggerName;
    String message;
}
