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
        LogMessage log = LogMessage.builder()
                .level("info")
                .loggerName("blog-service")
                .message(message)
                .build();
        kafka.send("info", log);
    }

    public void warn(String message) {
        LogMessage log = LogMessage.builder()
                .level("warn")
                .loggerName("blog-service")
                .message(message)
                .build();
        kafka.send("warn", log);
    }

    public void error(String message) {
        LogMessage log = LogMessage.builder()
                .level("error")
                .loggerName("blog-service")
                .message(message)
                .build();
        kafka.send("error", log);
    }
}