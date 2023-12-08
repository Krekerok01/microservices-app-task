package com.specificgroup.blog.service;

import com.specificgroup.blog.dto.kafka.UserDeletedEvent;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Provides methods for processing the input data from the controller and sending it to the repository
 */
public interface PostService {
    /**
     * Create a new post by user
     *
     * @param postRequest an object containing basic information
     * @param userId an id of the user who wants to create a post
     * @return a PostResponse object
     */
    PostResponse createPost(PostRequest postRequest, Long userId);

    /**
     * Find all posts from the database
     *
     * @param specification an object containing optional fields for filtering the search
     * @return a list of PostResponse objects
     */
    List<PostResponse> findAll(Specification<Post> specification);

    /**
     * Find post from the database by post id
     *
     * @return a PostResponse object
     */
    PostResponse findById(Long id);

    /**
     * Update a post by user(author)
     *
     * @param postRequest an object containing basic information
     * @param postId an id of the post being updated
     * @param userId an id of the user(author) who wants to update a post
     * @return a PostResponse object
     */
    Long updatePost(PostRequest postRequest, Long postId, Long userId);

    /**
     * Delete a post by user(author)
     *
     * @param id an id of the post being deleted
     * @param userId an id of the user who wants to delete post
     */
    void deletePost(Long id, Long userId);

    /**
     * Delete all posts for a specific user
     *
     * @param message a request from kafka the user service
     */
    void deletePostsByUserId(UserDeletedEvent message);

    /**
     * Find all posts for a specific user by his subscriptions
     *
     * @param requestUserId an id of the user to find posts for
     * @return a list of PostResponse objects
     */
    List<PostResponse> findSubscriptionsPostsByUserId(Long requestUserId);
}