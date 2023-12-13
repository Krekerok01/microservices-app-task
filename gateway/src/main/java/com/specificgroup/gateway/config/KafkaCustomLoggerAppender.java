package com.specificgroup.gateway.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.specificgroup.gateway.dto.LogEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaCustomLoggerAppender extends AppenderBase<ILoggingEvent> {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaCustomLoggerAppender.class);
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("custom logger");
        System.out.println(kafkaTemplate != null);
        kafkaTemplate.send("logs", LogEvent.builder()
                .level("warn")
                .loggerName("gateway")
                .message(eventObject.getMessage())
                .build());
        switch (eventObject.getLevel().levelStr) {
            case "WARN" -> logger.warn(eventObject.getMessage());
            case "ERROR" -> logger.error(eventObject.getMessage());
        }
    }

//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaCustomLoggerAppender.class);
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public KafkaCustomLoggerAppender() {
//    }
//
//    //    @Autowired
//    public KafkaCustomLoggerAppender(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    @Override
//    protected void append(ILoggingEvent eventObject) {
//        System.out.println("custom logger");
//        kafkaTemplate.send("logs", LogEvent.builder()
//                .level("warn")
//                .loggerName("gateway")
//                .message(eventObject.getMessage())
//                .build());
//        switch (eventObject.getLevel().levelStr) {
//            case "WARN" -> logger.warn(eventObject.getMessage());
//            case "ERROR" -> logger.error(eventObject.getMessage());
//        }
//    }
}