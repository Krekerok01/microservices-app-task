package com.specificgroup.logs;

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
