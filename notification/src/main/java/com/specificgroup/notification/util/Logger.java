package com.specificgroup.notification.util;

import com.specificgroup.notification.dto.LogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
@RequiredArgsConstructor
public class Logger {

    private final KafkaTemplate<String, Object> kafka;

    @Value("${spring.kafka.topics.notifications.logs}")
    private String LOGS_TOPIC;

    public void info(String message) {
        sendToKafka("info", message);
    }

    public void warn(String message) {
        sendToKafka("warn", message);
    }

    public void error(String message) {
        sendToKafka("error", message);
    }

    private void sendToKafka(String level, String message) {
        kafka.send(LOGS_TOPIC, LogEvent.builder()
                .level(level)
                .loggerName("notification-service")
                .message(message)
                .build());
    }
}