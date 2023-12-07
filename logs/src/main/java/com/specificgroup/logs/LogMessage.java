package com.specificgroup.logs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessage {
    String level;
    String loggerName;
    String message;
}
