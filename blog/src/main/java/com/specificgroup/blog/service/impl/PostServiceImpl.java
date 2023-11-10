package com.specificgroup.blog.service.impl;

import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Override
    public Post createPost(PostRequest postRequest, Long userId) {
        log.info("Creating of a new post: {}", postRequest);
        return null;
    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public Post findById(Long id) {
        return null;
    }

    @Override
    public Long updatePost(PostRequest postRequest) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }
}
