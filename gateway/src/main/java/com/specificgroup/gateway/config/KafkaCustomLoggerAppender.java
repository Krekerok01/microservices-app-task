package com.specificgroup.gateway.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Component
@Slf4j
public class KafkaCustomLoggerAppender extends AppenderBase<ILoggingEvent> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    public KafkaCustomLoggerAppender() {
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory());
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("Custom");
        boolean b = kafkaTemplate == null;
        System.out.println("Kafka == null: " + b);
        if (eventObject.getLevel().levelStr.equals("INFO")) {
            System.out.println(eventObject);
        } else {
            System.out.println(">>>>>>" + eventObject);
        }
    }
}