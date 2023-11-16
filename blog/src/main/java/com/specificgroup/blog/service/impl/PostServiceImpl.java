package com.specificgroup.blog.service.impl;

import com.specificgroup.blog.dto.kafka.BlogServiceResponseMessage;
import com.specificgroup.blog.dto.kafka.UserServiceMessage;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.exception.AccessDeniedException;
import com.specificgroup.blog.exception.EntityNotFoundException;
import com.specificgroup.blog.kafka.KafkaProducer;
import com.specificgroup.blog.repository.PostRepository;
import com.specificgroup.blog.service.PostService;
import com.specificgroup.blog.util.DateTimeUtil;
import com.specificgroup.blog.util.getter.UserInfoGetter;
import com.specificgroup.blog.util.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper mapper;
    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.topics.user.service.response.successful}")
    private String successfulResponseTopic;
    private final UserInfoGetter userInfoGetter;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, Long userId) {
        log.info("Creating a new post using the following information: {}", postRequest);

        LocalDateTime minskCurrentTime = DateTimeUtil.getMinskCurrentTime();
        Post post = Post.builder()
                .title(postRequest.getTitle().trim())
                .text(postRequest.getText().trim())
                .userId(userId)
                .creationDate(minskCurrentTime)
                .modificationDate(minskCurrentTime)
                .build();

        PostResponse postResponse = mapper.postToPostResponse(postRepository.save(post));
        postResponse.setUsername(userInfoGetter.getUsernameByUserId(postResponse.getUserId()));
        return postResponse;
    }

    @Override
    public List<PostResponse> findAll(Specification<Post> specification) {
        log.info("Finding all posts");
        return postRepository.findAll(specification)
                .stream()
                .map(post -> {
                        PostResponse postResponse = mapper.postToPostResponse(post);
                        postResponse.setUsername(userInfoGetter.getUsernameByUserId(postResponse.getUserId()));
                        return postResponse;
                    })
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse findById(Long id) {
        log.info("Finding post by id={}", id);
        Post post = findByIdOrThrowNoyFoundException(id);
        PostResponse postResponse = mapper.postToPostResponse(post);
        postResponse.setUsername(userInfoGetter.getUsernameByUserId(postResponse.getUserId()));
        return postResponse;
    }

    @Override
    @Transactional
    public Long updatePost(PostRequest postRequest, Long postId, Long userId) {
        log.info("Updating the post with id={} using following information: {}", postId, postRequest);

        Post post = findByIdOrThrowNoyFoundException(postId);

        accessVerification(post.getUserId(), userId);

        post.setText(postRequest.getText().trim());
        post.setTitle(postRequest.getTitle().trim());
        post.setModificationDate(DateTimeUtil.getMinskCurrentTime());

        postRepository.save(post);
        return postId;
    }

    @Override
    public void deletePost(Long id, Long userId) {
        log.info("Deleting the post with id={}", id);
        Post post = findByIdOrThrowNoyFoundException(id);
        accessVerification(post.getUserId(), userId);
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void deletePostsByUserId(UserServiceMessage message) {
        log.info("Deleting posts with userId={}", message.getUserId());
        postRepository.deleteAllByUserId(message.getUserId());
        BlogServiceResponseMessage responseMessage = BlogServiceResponseMessage.builder()
                .deletedUserId(message.getUserId())
                .message("Request successfully processed.")
                .build();
        kafkaProducer.notify(successfulResponseTopic, responseMessage);
    }

    private Post findByIdOrThrowNoyFoundException(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    private void accessVerification(Long savedUserId, Long currentUserId) {
        if (!savedUserId.equals(currentUserId)) {
            throw new AccessDeniedException("Access denied.");
        }
    }
}