package com.specificgroup.notification.util;

import com.specificgroup.notification.dto.kafka.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
@RequiredArgsConstructor
public class Logger {

    private final KafkaTemplate<String, Object> kafka;

    public void info(String message) {
        LogMessage log = LogMessage.builder()
                .level("info")
                .loggerName("notification-service")
                .message(message)
                .build();
        kafka.send("info", log);
    }

    public void warn(String message) {
        LogMessage log = LogMessage.builder()
                .level("warn")
                .loggerName("notification-service")
                .message(message)
                .build();
        kafka.send("warn", log);
    }

    public void error(String message) {
        LogMessage log = LogMessage.builder()
                .level("error")
                .loggerName("notification-service")
                .message(message)
                .build();
        kafka.send("error", log);
    }
}