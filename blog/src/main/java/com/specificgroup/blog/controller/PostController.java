package com.specificgroup.blog.controller;

import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.repository.PostSpecification;
import com.specificgroup.blog.util.security.JwtUtil;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create post", description = "Creation post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such user in the database(user service error)",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest, HttpServletRequest httpRequest){
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return new ResponseEntity<>(postService.createPost(postRequest, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all posts(by specification)", description = "Getting all posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
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

    @Operation(summary = "Get all posts of the user and his subscriptions", description = "Getting posts of the user and his subscriptions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no specific info in the subscription or user services",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/subscriptions")
    public List<PostResponse> findSubscriptionsPosts(HttpServletRequest httpRequest){
        Long requestUserId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return postService.findSubscriptionsPostsByUserId(requestUserId);
    }

    @Operation(summary = "Get post by id", description = "Getting post by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There is no such post in the database",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
    @GetMapping("/{postId}")
    public PostResponse findPostById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @Operation(summary = "Update post", description = "Updating post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error: Client request error(fields validation)",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Client can modify only his posts",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There are no necessary records in the database",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{postId}")
    public Long updatePost(@PathVariable Long postId, @RequestBody @Valid PostRequest postRequest,
                           HttpServletRequest httpRequest){
        Long userId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return postService.updatePost(postRequest, postId, userId);
    }

    @Operation(summary = "Delete post", description = "Deletion post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful request",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: User wasn't authorized",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Client can delete only his posts",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: There are no necessary records in the database",
                    content = @Content),
            @ApiResponse(responseCode = "503", description = "Error: Problem with access to the external service",
                    content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
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