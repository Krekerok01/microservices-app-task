package com.specificgroup.blog.controller;


import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostRequest postRequest){
        Long userId = 1L;
        return new ResponseEntity<>(postService.createPost(postRequest, userId), HttpStatus.CREATED);
    }
}
