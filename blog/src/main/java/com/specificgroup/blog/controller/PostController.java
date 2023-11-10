package com.specificgroup.blog.controller;


import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    public List<Post> findPosts(){
        return postService.findAll();
    }

    @GetMapping("/{postId}")
    public Post findPostById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PatchMapping("/{postId}")
    public Long updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequest postRequest){
        return postService.updatePost(postRequest, postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
