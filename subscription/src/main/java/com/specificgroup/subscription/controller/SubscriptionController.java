package com.specificgroup.subscription.controller;

import com.specificgroup.subscription.entity.Subscription;
import com.specificgroup.subscription.service.SubscriptionService;
import com.specificgroup.subscription.util.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{userPublisherId}")
    public ResponseEntity<Long> createSubscription(@PathVariable("userPublisherId") Long userPublisherId,
                                                   HttpServletRequest httpRequest){
        Long userSubscriberId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        return new ResponseEntity<>(subscriptionService.createSubscription
                (userSubscriberId, userPublisherId), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Subscription> findAllSubscriptions(){
        return subscriptionService.getAllSubscriptions();
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long subscriptionId, HttpServletRequest httpRequest) {
        Long userSubscriberId = getUserIdFromTheTokenInTheHttpRequest(httpRequest);
        subscriptionService.deleteSubscription(subscriptionId, userSubscriberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getUserIdFromTheTokenInTheHttpRequest(HttpServletRequest httpRequest) {
        return JwtUtil.getUserIdFromToken(httpRequest.getHeader(HttpHeaders.AUTHORIZATION));
    }
}