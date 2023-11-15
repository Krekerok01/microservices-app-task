package com.specificgroup.blog.service.impl;

import com.specificgroup.blog.service.KafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumerImpl implements KafkaConsumer {
    private final String TOPIC_BLOG_USER = "blog-user";
    @Override
    @KafkaListener(topics = TOPIC_BLOG_USER)
    public void consumeUserPostsDeleting(Long userId) {

    }
}
