package com.specificgroup.blog.service.impl;

import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.exception.EntityNotFoundException;
import com.specificgroup.blog.repository.PostRepository;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post createPost(PostRequest postRequest, Long userId) {
        log.info("Creating of a new post: {}", postRequest);
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .text(postRequest.getText())
                .userId(userId)
                .creationDate(LocalDateTime.now())
                .modificationDate(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        log.info("Finding all posts");
        return postRepository.findAll();
    }

    @Override
    public Post findById(Long id) {
        log.info("Finding post by id={}", id);
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Override
    @Transactional
    public Long updatePost(PostRequest postRequest, Long postId) {
        log.info("Updating post info: {}", postRequest);

        Post post = findById(postId);
        post.setText(postRequest.getText());
        post.setTitle(postRequest.getTitle());
        post.setModificationDate(LocalDateTime.now());

        return postId;
    }

    @Override
    public void deletePost(Long id) {
        log.info("Deleting post with id={}", id);
        postRepository.deleteById(id);
    }
}
