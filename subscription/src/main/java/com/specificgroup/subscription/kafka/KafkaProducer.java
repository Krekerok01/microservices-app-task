package com.specificgroup.subscription.kafka;

import com.specificgroup.subscription.dto.kafka.SuccessfullyDeletedSubscriptionEvent;
import com.specificgroup.subscription.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Provides methods for sending data to the Kafka.
 */
@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafka;
    private final Logger logger;

    public void notify(String topicName, SuccessfullyDeletedSubscriptionEvent event) {
        kafka.send(topicName, event);
        logger.info("Event " + event +
                " has been successfully sent to the " + topicName +
                " topic");
    }
}