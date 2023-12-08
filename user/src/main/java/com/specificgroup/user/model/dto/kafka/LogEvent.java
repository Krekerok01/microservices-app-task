package com.specificgroup.user.model.dto.kafka;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class LogEvent {
    String level;
    String loggerName;
    String message;
}