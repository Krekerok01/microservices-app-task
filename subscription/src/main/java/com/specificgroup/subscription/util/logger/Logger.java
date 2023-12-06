package com.specificgroup.subscription.util.logger;

import com.specificgroup.subscription.dto.LogMessage;
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
                .loggerName("subscription-service")
                .message(message)
                .build();
        kafka.send("info", log);
    }

    public void warn(String message) {
        LogMessage log = LogMessage.builder()
                .level("warn")
                .loggerName("subscription-service")
                .message(message)
                .build();
        kafka.send("warn", log);
    }

    public void error(String message) {
        LogMessage log = LogMessage.builder()
                .level("error")
                .loggerName("subscription-service")
                .message(message)
                .build();
        kafka.send("error", log);
    }
}