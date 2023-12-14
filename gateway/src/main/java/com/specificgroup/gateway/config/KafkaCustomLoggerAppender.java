package com.specificgroup.gateway.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
//@Slf4j
public class KafkaCustomLoggerAppender extends AppenderBase<ILoggingEvent> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaCustomLoggerAppender.class);

    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("Custom");
        boolean b = kafkaTemplate == null;
        System.out.println("Kafka == null: " + b);
        if (eventObject.getLevel().levelStr.equals("INFO")) {
            System.out.println(eventObject.toString());
        } else {
            System.out.println(">>>>>>" + eventObject.toString());
        }
    }
}