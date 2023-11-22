package com.specificgroup.subscription;

import com.specificgroup.subscription.dto.UserServiceMessage;
import com.specificgroup.subscription.entity.Subscription;
import com.specificgroup.subscription.exception.AccessDeniedException;
import com.specificgroup.subscription.exception.EntityNotFoundException;
import com.specificgroup.subscription.kafka.KafkaProducer;
import com.specificgroup.subscription.repository.SubscriptionRepository;
import com.specificgroup.subscription.service.impl.SubscriptionServiceImpl;
import com.specificgroup.subscription.util.getter.UserInfoGetter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private UserInfoGetter userInfoGetter;
    @Mock
    private KafkaProducer kafkaProducer;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    @DisplayName("Create a subscription. Successful request")
    void createSubscription_SuccessfulRequest(){
        Long userSubscriberId = 1L;
        Long userPublisherId = 2L;

        Subscription subscription = buildSubscription(1L, 1L, 2L);

        doReturn(subscription).when(subscriptionRepository).save(any(Subscription.class));
        doReturn(false).when(subscriptionRepository).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);
        doReturn(true).when(userInfoGetter).existsUserById(userPublisherId);

        Long resultResponse = subscriptionService.createSubscription(userSubscriberId, userPublisherId);

        assertNotNull(resultResponse);
        assertEquals(1L, resultResponse);
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(subscriptionRepository, times(1)).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);
        verifyNoMoreInteractions(subscriptionRepository);
        verify(userInfoGetter, times(1)).existsUserById(userPublisherId);
        verifyNoMoreInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Create a subscription.Throw AccessDeniedException")
    void createSubscription_ThrowAccessDeniedException(){
        Long userSubscriberId = 1L;
        Long userPublisherId = 1L;
        String expectedMessage = "Impossible to subscribe to yourself";

        AccessDeniedException resultException = assertThrowsExactly(AccessDeniedException.class, () -> {
            subscriptionService.createSubscription(userSubscriberId, userPublisherId);
        });

        assertNotNull(resultException);
        assertEquals(expectedMessage, resultException.getLocalizedMessage());
        verifyNoInteractions(subscriptionRepository);
        verifyNoInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Create a subscription.Throw EntityExistsException")
    void createSubscription_ThrowEntityExistsException(){
        Long userSubscriberId = 1L;
        Long userPublisherId = 2L;
        String expectedMessage = "This subscription already exists";

        doReturn(true).when(subscriptionRepository).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);

        EntityExistsException resultException = assertThrowsExactly(EntityExistsException.class, () -> {
            subscriptionService.createSubscription(userSubscriberId, userPublisherId);
        });

        assertNotNull(resultException);
        assertEquals(expectedMessage, resultException.getLocalizedMessage());
        verify(subscriptionRepository, times(1)).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);
        verifyNoMoreInteractions(subscriptionRepository);
        verifyNoInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Create a subscription.Throw EntityNotFoundException")
    void createSubscription_ThrowEntityNotFoundException(){
        Long userSubscriberId = 1L;
        Long userPublisherId = 2L;
        String expectedMessage = "Publisher with id=" + userPublisherId + " not found";

        doReturn(false).when(subscriptionRepository).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);
        doReturn(false).when(userInfoGetter).existsUserById(userPublisherId);

        EntityNotFoundException resultException = assertThrowsExactly(EntityNotFoundException.class, () -> {
            subscriptionService.createSubscription(userSubscriberId, userPublisherId);
        });

        assertNotNull(resultException);
        assertEquals(expectedMessage, resultException.getLocalizedMessage());
        verify(subscriptionRepository, times(1)).existsByUserSubscriberIdAndUserPublisherId(userSubscriberId, userPublisherId);
        verifyNoMoreInteractions(subscriptionRepository);
        verify(userInfoGetter, times(1)).existsUserById(userPublisherId);
        verifyNoMoreInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Find all subscriptions. Successful request")
    void findAllTest_SuccessfulRequest(){
        Subscription subscription1 = buildSubscription(1L, 2L, 3L);
        Subscription subscription2 = buildSubscription(2L, 3L, 4L);
        List<Subscription> subscriptions = List.of(subscription1, subscription2);

        doReturn(subscriptions).when(subscriptionRepository).findAll();

        List<Subscription> resultResponseList = subscriptionService.getAllSubscriptions();

        assertNotNull(resultResponseList);
        assertEquals(subscriptions.size(), resultResponseList.size());
        verify(subscriptionRepository, times(1)).findAll();
        verifyNoMoreInteractions(subscriptionRepository);
        verifyNoInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Find all subscriptions by subscriber Id. Successful request")
    void findAllSubscriptionsBySubscriberIdTest_SuccessfulRequest(){
        Subscription subscription1 = buildSubscription(1L, 2L, 3L);
        Subscription subscription2 = buildSubscription(2L, 2L, 4L);
        List<Subscription> subscriptions = List.of(subscription1, subscription2);

        doReturn(subscriptions).when(subscriptionRepository).findAllByUserSubscriberId(2L);

        List<Long> resultResponseList = subscriptionService.getSubscriptionsBySubscriberId(2L);

        assertNotNull(resultResponseList);
        assertEquals(subscriptions.size(), resultResponseList.size());
        verify(subscriptionRepository, times(1)).findAllByUserSubscriberId(2L);
        verifyNoMoreInteractions(subscriptionRepository);
        verifyNoInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Delete subscription. Successful request")
    void deleteSubscriptionTest_SuccessfulRequest(){
        Long subscriptionId = 1L;
        Long userSubscriberId = 1L;
        Subscription subscription =buildSubscription(1L, 1L, 2L);

        doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(subscriptionId);

        boolean result = assertDoesNotThrow(() -> {
            subscriptionService.deleteSubscription(subscriptionId, userSubscriberId);
            return true;});

        assertTrue(result);
        verify(subscriptionRepository, times(1)).findById(subscriptionId);
        verify(subscriptionRepository, times(1)).delete(subscription);
        verifyNoMoreInteractions(subscriptionRepository);
    }

    @Test
    @DisplayName("Delete subscription. Throw AccessDeniedException")
    void deleteSubscriptionTest_ThrowAccessDeniedException(){
        Long subscriptionId = 1L;
        Long userSubscriberId = 1L;
        String expectedMessage = "Access denied";
        Subscription subscription =buildSubscription(1L, 2L, 2L);

        doReturn(Optional.of(subscription)).when(subscriptionRepository).findById(subscriptionId);

        AccessDeniedException resultException = assertThrowsExactly(AccessDeniedException.class, () -> {
            subscriptionService.deleteSubscription(subscriptionId, userSubscriberId);
        });

        assertNotNull(resultException);
        assertEquals(expectedMessage, resultException.getLocalizedMessage());
        verify(subscriptionRepository, times(1)).findById(subscriptionId);
        verifyNoMoreInteractions(subscriptionRepository);
        verifyNoInteractions(userInfoGetter);
    }

    @Test
    @DisplayName("Delete subscriptions by user id. Successful request")
    void deleteSubscriptionsByUserId_SuccessfulRequest(){
        Long userId = 1L;
        UserServiceMessage userServiceMessage = UserServiceMessage.builder().userId(userId).build();

        boolean result = assertDoesNotThrow(() -> {
            subscriptionService.deleteSubscriptionsByUserId(userServiceMessage);
            return true;});

        assertTrue(result);
        verify(subscriptionRepository, times(1)).deleteAllByUserSubscriberId(userId);
        verifyNoMoreInteractions(subscriptionRepository);
    }


    private static Subscription buildSubscription(Long subscriptionId, Long userSubscriberId, Long userPublisherId) {
        return Subscription.builder()
                .subscriptionId(subscriptionId)
                .userSubscriberId(userSubscriberId)
                .userPublisherId(userPublisherId)
                .build();
    }
}