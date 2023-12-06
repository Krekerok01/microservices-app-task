package com.specificgroup.logs;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Provides methods for listening Kafka topics, receiving data and sending for further processing.
 */
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "${spring.kafka.topics.logging.info}")
    public void consumeInfoLogs(LogMessage message)  {
        System.out.println(message);
    }
}