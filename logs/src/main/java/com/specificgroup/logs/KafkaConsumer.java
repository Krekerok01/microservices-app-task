package com.specificgroup.logs;


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

    @KafkaListener(topics = "${spring.kafka.topics.logging.info}")
    public void consumeInfoLogs(LogMessage message)  {
        Marker marker = MarkerFactory.getMarker(message.loggerName);
        switch (message.level) {
            case "info" -> log.info(marker, message.message);
            case "error" -> log.error(marker, message.message);
            case "warn" -> log.warn(marker, message.message);
        }
    }
}