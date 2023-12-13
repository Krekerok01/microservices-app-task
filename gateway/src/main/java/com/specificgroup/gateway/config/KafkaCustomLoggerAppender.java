package com.specificgroup.gateway.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class KafkaCustomLoggerAppender extends AppenderBase<ILoggingEvent> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("custom logger");
        System.out.println(kafkaTemplate != null);
    }

    @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLoggerList()
                .forEach(logger -> logger
                        .addAppender(KafkaCustomLoggerAppender.this));

        setContext(context);
        start();
    }
}