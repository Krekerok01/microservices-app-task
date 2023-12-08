package com.specificgroup.subscription.util.logger;

import com.specificgroup.subscription.dto.kafka.LogEvent;
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
        sendToKafka( "info", message);
    }

    public void warn(String message) {
        sendToKafka("warn", message);
    }

    public void error(String message) {
        sendToKafka("error", message);
    }

    private void sendToKafka(String level, String message) {
        kafka.send("logs", LogEvent.builder()
                .level(level)
                .loggerName("blog-service")
                .message(message)
                .build());
    }
}