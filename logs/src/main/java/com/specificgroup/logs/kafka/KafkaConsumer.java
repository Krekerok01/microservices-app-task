package com.specificgroup.logs.kafka;


import com.specificgroup.logs.kafka.dto.LogEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Provides methods for listening Kafka topics, receiving data and sending for further processing.
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topics.logging}")
    public void consumeInfoLogs(LogEvent event)  {
        Marker marker = MarkerFactory.getMarker(event.getLoggerName());
        String message = event.getMessage();
        switch (event.getLevel()) {
            case "info" -> log.info(marker, message);
            case "error" -> log.error(marker, message);
            case "warn" -> log.warn(marker, message);
        }
    }
}