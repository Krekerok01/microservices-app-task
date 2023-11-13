package com.specificgroup.blog.service;

import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest postRequest, Long userId);
    List<PostResponse> findAll();
    PostResponse findById(Long id);
    Long updatePost(PostRequest postRequest, Long postId, Long userId);
    void deletePost(Long id, Long userId);
}