package com.specificgroup.blog.util.logger;

import com.specificgroup.blog.dto.kafka.LogMessage;
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
        LogMessage log = LogMessage.builder()
                .level(level)
                .loggerName("blog-service")
                .message(message)
                .build();
        kafka.send("logs", log);
    }
}