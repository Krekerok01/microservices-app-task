package com.specificgroup.blog.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name("blog-user")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
