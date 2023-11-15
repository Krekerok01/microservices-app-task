package com.specificgroup.blog.consumer;

import com.specificgroup.blog.dto.kafka.UserServiceMessage;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final PostService postService;

    @KafkaListener(topics = "${spring.kafka.topics.user}")
    public void consumeUserPostsDeleting(UserServiceMessage message)  {
        log.info("Received a request to delete posts for user {}", message);
        postService.deletePostsByUserId(message);
    }
}