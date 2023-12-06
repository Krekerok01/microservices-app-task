package com.specificgroup.blog.kafka;

import com.specificgroup.blog.dto.kafka.UserServiceMessage;
import com.specificgroup.blog.service.PostService;
import com.specificgroup.blog.util.logger.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Provides methods for listening Kafka topics, receiving data and sending for further processing.
 */
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final PostService postService;
    private final Logger logger;

    @KafkaListener(topics = "${spring.kafka.topics.user.service.request}")
    public void consumeUserPostsDeleting(UserServiceMessage message)  {
        logger.info("Received a request to delete posts for user " + message);
        postService.deletePostsByUserId(message);
    }
}