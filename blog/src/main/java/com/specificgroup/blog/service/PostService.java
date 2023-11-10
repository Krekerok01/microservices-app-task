package com.specificgroup.blog.service;

import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;

import java.util.List;

public interface PostService {
    Post createPost(PostRequest postRequest, Long userId);
    List<Post> findAll();
    Post findById(Long id);
    Long updatePost(PostRequest postRequest);
    void deletePost(Long id);
}
