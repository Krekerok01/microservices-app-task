package com.specificgroup.blog.service.impl;

import com.specificgroup.blog.dto.kafka.SuccessfullyDeletedPostsEvent;
import com.specificgroup.blog.dto.kafka.UserDeletedEvent;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.exception.AccessDeniedException;
import com.specificgroup.blog.exception.EntityNotFoundException;
import com.specificgroup.blog.kafka.KafkaProducer;
import com.specificgroup.blog.repository.PostRepository;
import com.specificgroup.blog.service.PostService;
import com.specificgroup.blog.util.DateTimeUtil;
import com.specificgroup.blog.util.getter.InfoGetter;
import com.specificgroup.blog.util.logger.Logger;
import com.specificgroup.blog.util.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.specificgroup.blog.util.Constants.Message.*;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final KafkaProducer kafkaProducer;
    private final Logger logger;

    @Value("${spring.kafka.topics.user.service.response.successful}")
    private String successfulResponseTopic;
    private final InfoGetter infoGetter;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, Long userId) {
        logger.info("Creating a new post using the following information: " + postRequest);

        LocalDateTime minskCurrentTime = DateTimeUtil.getMinskCurrentTime();
        Post post = Post.builder()
                .title(postRequest.getTitle().trim())
                .text(postRequest.getText().trim())
                .userId(userId)
                .creationDate(minskCurrentTime)
                .modificationDate(minskCurrentTime)
                .build();

        PostResponse postResponse = mapper.postToPostResponse(postRepository.save(post));
        postResponse.setUsername(infoGetter.getUsernameByUserId(postResponse.getUserId()));
        return postResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponse> findAll(Specification<Post> specification) {
        logger.info("Finding all posts");
        return postRepository.findAll(specification)
                .stream()
                .map(post -> {
                        PostResponse postResponse = mapper.postToPostResponse(post);
                        postResponse.setUsername(infoGetter.getUsernameByUserId(postResponse.getUserId()));
                        return postResponse;
                    })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PostResponse> findSubscriptionsPostsByUserId(Long requestUserId) {
        logger.info("Finding all posts for userId=" + requestUserId);
        List<Long> subscriptionIds = infoGetter.getSubscriptionIdsListBySubscriberId(requestUserId);
        subscriptionIds.add(requestUserId);
        return postRepository.findAllByUserIdIn(subscriptionIds)
                .stream()
                .map(post -> {
                    PostResponse postResponse = mapper.postToPostResponse(post);
                    postResponse.setUsername(infoGetter.getUsernameByUserId(postResponse.getUserId()));
                    return postResponse;
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostResponse findById(Long id) {
        logger.info("Finding post by id=" + id);
        Post post = findByIdOrThrowNoyFoundException(id);
        PostResponse postResponse = mapper.postToPostResponse(post);
        postResponse.setUsername(infoGetter.getUsernameByUserId(postResponse.getUserId()));
        return postResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Long updatePost(PostRequest postRequest, Long postId, Long userId) {
        logger.info("Updating the post with id=" + postId
                + " using following information: " + postRequest);

        Post post = findByIdOrThrowNoyFoundException(postId);

        accessVerification(post.getUserId(), userId);

        post.setText(postRequest.getText().trim());
        post.setTitle(postRequest.getTitle().trim());
        post.setModificationDate(DateTimeUtil.getMinskCurrentTime());

        postRepository.save(post);
        return postId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePost(Long id, Long userId) {
        logger.info("Deleting the post with id=" + id);
        Post post = findByIdOrThrowNoyFoundException(id);
        accessVerification(post.getUserId(), userId);
        postRepository.delete(post);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deletePostsByUserId(UserDeletedEvent message) {
        logger.info("Deleting posts with userId=" + message.getUserId());
        postRepository.deleteAllByUserId(message.getUserId());
        SuccessfullyDeletedPostsEvent event = SuccessfullyDeletedPostsEvent.builder()
                .deletedUserId(message.getUserId())
                .message(SERVICE_REQUEST_SUCCESSFULLY_PROCESSED)
                .build();
        kafkaProducer.notify(successfulResponseTopic, event);
    }

    private Post findByIdOrThrowNoyFoundException(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format
                        (RESOURCE_NOT_FOUND, "Post")));
    }

    private void accessVerification(Long savedUserId, Long currentUserId) {
        if (!savedUserId.equals(currentUserId)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }
}