package com.specificgroup.blog.service;

public interface KafkaConsumer {
    void consumeUserPostsDeleting(Long userId);
}
