package com.specificgroup.blog.controller;

import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.repository.PostSpecification;
import com.specificgroup.blog.util.JwtUtil;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest, HttpServletRequest httpRequest){
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return new ResponseEntity<>(postService.createPost(postRequest, userId), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PostResponse> findPosts(@RequestParam(value = "userId", required = false) Long userId,
                                        @RequestParam(value = "title", required = false) String title,
                                        @RequestParam(value = "creationDate", required = false)
                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate creationDate,
                                        @RequestParam(value = "modificationDate", required = false)
                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate modificationDate){
        return postService
                .findAll(PostSpecification.getSpecification(userId, title, creationDate, modificationDate));
    }

    @GetMapping("/{postId}")
    public PostResponse findPostById(@PathVariable Long postId) {
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
        return JwtUtil.getUserIdFromToken(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
    }
}