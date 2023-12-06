package com.specificgroup.blog.dto.kafka;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class LogMessage {
    String level;
    String loggerName;
    String message;
}