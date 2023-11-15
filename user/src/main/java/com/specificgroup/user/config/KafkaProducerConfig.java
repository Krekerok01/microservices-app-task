package com.specificgroup.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name("blog-user")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
