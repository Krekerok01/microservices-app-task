package com.specificgroup.blog.service;

import com.specificgroup.blog.dto.kafka.UserDeletedEvent;
import com.specificgroup.blog.dto.request.PostRequest;
import com.specificgroup.blog.dto.response.PostResponse;
import com.specificgroup.blog.entity.Post;
import com.specificgroup.blog.exception.AccessDeniedException;
import com.specificgroup.blog.kafka.KafkaProducer;
import com.specificgroup.blog.repository.PostRepository;
import com.specificgroup.blog.repository.PostSpecification;
import com.specificgroup.blog.service.impl.PostServiceImpl;
import com.specificgroup.blog.util.DateTimeUtil;
import com.specificgroup.blog.util.getter.InfoGetter;
import com.specificgroup.blog.util.logger.Logger;
import com.specificgroup.blog.util.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;
    private PostMapper noMockedMapper = Mappers.getMapper(PostMapper.class);
    @Mock
    private InfoGetter infoGetter;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private Logger logger;
    @InjectMocks
    private PostServiceImpl postService;

    @Test
    @DisplayName("Create a post. Successful request")
    void createPostTest_SuccessfulRequest(){
        PostRequest postRequest = new PostRequest("Title", "Interesting text");
        Long userId = 1L;

        Post post =buildPost(1L, userId, postRequest.getTitle(), postRequest.getText());

        doReturn(noMockedMapper.postToPostResponse(post)).when(postMapper).postToPostResponse(post);
        doReturn(post).when(postRepository).save(any(Post.class));
        doReturn("username").when(infoGetter).getUsernameByUserId(userId);

        PostResponse resultResponse = postService.createPost(postRequest, userId);

        assertNotNull(resultResponse);
        assertEquals(post.getPostId(), resultResponse.getPostId());
        assertEquals("username", resultResponse.getUsername());
        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
        verify(infoGetter, times(1)).getUsernameByUserId(userId);
        verifyNoMoreInteractions(infoGetter);

    }

    @Test
    @DisplayName("Find all posts. Full list. Successful request")
    void findAll_NotEmptyList(){
        Long userId = 1L;
        Specification<Post> specification = PostSpecification.getSpecification(userId, null, null, null);

        Post post1 = buildPost(1L, userId, "First post", "First text");
        Post post2 =  buildPost(2L, userId, "Second post", "Second text");
        List<Post> posts = List.of(post1, post2);

        doReturn(posts).when(postRepository).findAll(specification);
        doReturn(noMockedMapper.postToPostResponse(post1)).when(postMapper).postToPostResponse(post1);
        doReturn(noMockedMapper.postToPostResponse(post2)).when(postMapper).postToPostResponse(post2);

        List<PostResponse> resultResponseList = postService.findAll(specification);

        assertNotNull(resultResponseList);
        assertEquals(posts.size(), resultResponseList.size());
        verify(postRepository, times(1)).findAll(specification);
        verifyNoMoreInteractions(postRepository);
        verify(infoGetter, times(2)).getUsernameByUserId(userId);
    }

    @Test
    @DisplayName("Find all posts. Empty list. Successful request")
    void findAll_EmptyList(){
        Long userId = 1L;
        Specification<Post> specification = PostSpecification.getSpecification(userId, null, null, null);

        List<Post> posts = new ArrayList<>();

        doReturn(posts).when(postRepository).findAll(specification);

        List<PostResponse> resultResponseList = postService.findAll(specification);

        assertNotNull(resultResponseList);
        assertEquals(posts.size(), resultResponseList.size());
        verify(postRepository, times(1)).findAll(specification);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(infoGetter);
        verifyNoInteractions(postMapper);
    }

    @Test
    @DisplayName("Find post by id. Successful request")
    void findByIdTest_SuccessfulRequest(){
        Long postId = 1L;
        String username = "username";
        Post post = buildPost(postId, 1L, "Title", "Interesting text");

        doReturn(Optional.of(post)).when(postRepository).findById(postId);
        doReturn(noMockedMapper.postToPostResponse(post)).when(postMapper).postToPostResponse(post);
        doReturn(username).when(infoGetter).getUsernameByUserId(anyLong());

        PostResponse resultResponse = postService.findById(postId);

        assertNotNull(resultResponse);
        assertEquals(post.getTitle(), resultResponse.getTitle());
        assertEquals(username, resultResponse.getUsername());
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
        verify(infoGetter, times(1)).getUsernameByUserId(anyLong());
    }

    @Test
    @DisplayName("Find subscriptions posts by user id. Successful request")
    void findSubscriptionsPostsByUserIdTest(){
        Long requestUserId = 1L;
        List<Long> subscriptionIds = new ArrayList<>();
        subscriptionIds.add(2L);
        Post post1 = buildPost(1L, requestUserId, "First post", "First text");
        Post post2 =  buildPost(2L, 2L, "Second post", "Second text");
        List<Post> posts = List.of(post1, post2);

        doReturn(subscriptionIds).when(infoGetter).getSubscriptionIdsListBySubscriberId(requestUserId);
        doReturn(posts).when(postRepository).findAllByUserIdIn(subscriptionIds);
        doReturn(noMockedMapper.postToPostResponse(post1)).when(postMapper).postToPostResponse(post1);
        doReturn(noMockedMapper.postToPostResponse(post2)).when(postMapper).postToPostResponse(post2);

        List<PostResponse> resultSubscriptionsPosts = postService.findSubscriptionsPostsByUserId(requestUserId);
        System.out.println(resultSubscriptionsPosts);

        assertNotNull(resultSubscriptionsPosts);
        assertEquals(posts.size(), resultSubscriptionsPosts.size());
        verify(postRepository, times(1)).findAllByUserIdIn(subscriptionIds);
        verifyNoMoreInteractions(postRepository);
        verify(infoGetter, times(2)).getUsernameByUserId(anyLong());
    }

    @Test
    @DisplayName("Update a post. Successful request")
    void updatePostTest_SuccessfulRequest(){
        PostRequest postRequest = new PostRequest("New title", "New Interesting text");
        Long postId = 1L;
        Long userId = 1L;

        Post post = buildPost(postId, userId, "Old title", "Old Interesting text");
        doReturn(Optional.of(post)).when(postRepository).findById(postId);
        Long resultPostId = postService.updatePost(postRequest, postId, userId);

        assertNotNull(resultPostId);
        assertEquals(postId, resultPostId);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(any(Post.class));
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Delete post. Successful request")
    void deletePostTest_SuccessfulRequest(){
        Long postId = 1L;
        Long userId = 1L;
        Post post = buildPost(postId, userId, "Title", "Interesting test");

        doReturn(Optional.of(post)).when(postRepository).findById(postId);

        boolean result = assertDoesNotThrow(() -> {
            postService.deletePost(postId, userId);
            return true;});

        assertTrue(result);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Delete post. Throw AccessDeniedException")
    void deletePostTest_ThrowAccessDeniedException(){
        Long postId = 1L;
        Long userId = 1L;
        Post post = buildPost(postId, userId, "Title", "Interesting test");

        doReturn(Optional.of(post)).when(postRepository).findById(postId);

        assertThrowsExactly(AccessDeniedException.class, () -> {
            postService.deletePost(postId, 99L);
        });
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    @DisplayName("Delete post by user id. Successful request")
    void deletePostByUserId_SuccessfulRequest(){
        Long userId = 1L;
        UserDeletedEvent userDeletedEvent = UserDeletedEvent.builder().userId(userId).build();

        boolean result = assertDoesNotThrow(() -> {
            postService.deletePostsByUserId(userDeletedEvent);
            return true;});
        assertTrue(result);
        verify(postRepository, times(1)).deleteAllByUserId(userId);
        verifyNoMoreInteractions(postRepository);
    }

    private static Post buildPost(Long postId, Long userId, String title, String text) {
        LocalDateTime minskCurrentTime = DateTimeUtil.getMinskCurrentTime();
        return Post.builder()
                .postId(postId)
                .userId(userId)
                .title(title)
                .text(text)
                .creationDate(minskCurrentTime)
                .modificationDate(minskCurrentTime)
                .build();
    }
}