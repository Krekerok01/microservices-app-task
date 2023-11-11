package com.specificgroup.blog.controller;

import com.specificgroup.blog.util.JwtUtil;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody @Valid PostRequest postRequest, HttpServletRequest httpRequest){
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
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
    public Long updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequest postRequest,
                           HttpServletRequest httpRequest){
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return postService.updatePost(postRequest, postId, userId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, HttpServletRequest httpRequest) {
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        postService.deletePost(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getUserIdFromTheTokenInTheHttpRequest(HttpServletRequest httpRequest) {
        String userId = JwtUtil.getUserIdFromToken(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
        return Long.parseLong(userId);
    }
}