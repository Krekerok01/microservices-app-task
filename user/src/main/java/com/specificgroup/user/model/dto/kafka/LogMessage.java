package com.specificgroup.user.model.dto.kafka;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class LogMessage {
    String level;
    String loggerName;
    String message;
}